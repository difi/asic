package no.difi.asic.encryption;

import no.difi.asic.Asic;
import no.difi.asic.api.DecryptionFilter;
import no.difi.asic.builder.Properties;
import no.difi.asic.code.EncryptionAlgorithm;
import no.difi.asic.lang.AsicException;
import no.difi.asic.util.BCUtil;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class CmsDecryptionEnvelopedDataFilter extends CmsDecryptionAbstractFilter {

    public static final DecryptionFilter INSTANCE = new CmsDecryptionEnvelopedDataFilter();

    /**
     * Private constructor.
     */
    private CmsDecryptionEnvelopedDataFilter() {
        // No action.
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("Duplicates")
    @Override
    public InputStream createFilter(InputStream inputStream, Properties properties) throws IOException {
        try {
            // Create envelope data
            CMSEnvelopedDataParser parser = new CMSEnvelopedDataParser(inputStream);

            // Verify valid encryption algorithm is used.
            if (properties.get(Asic.DECRYPTION_ALGORITHM).stream()
                    .map(EncryptionAlgorithm::getOid)
                    .map(ASN1ObjectIdentifier::getId)
                    .noneMatch(parser.getEncryptionAlgOID()::equals))
                throw new AsicException(String.format("Invalid encryption '%s' was used.", parser.getEncryptionAlgOID()));

            RecipientInformation recipient = null;
            PrivateKey privateKey = null;

            // Detecting private key for decryption.
            for (KeyStore.PrivateKeyEntry keyEntry : properties.get(Asic.DECRYPTION_CERTIFICATES)) {
                X509Certificate certificate = (X509Certificate) keyEntry.getCertificate();
                for (RecipientInformation r : parser.getRecipientInfos().getRecipients()) {
                    KeyTransRecipientId recipientId = (KeyTransRecipientId) r.getRID();

                    if (recipient == null && recipientId.getSerialNumber().equals(certificate.getSerialNumber())) {
                        recipient = r;
                        privateKey = keyEntry.getPrivateKey();

                        break;
                    }
                }
            }

            if (recipient == null)
                throw new AsicException("Unable to find certificate for decryption.");

            // Return decrypted stream.
            return recipient.getContentStream(new JceKeyTransEnvelopedRecipient(privateKey)
                    .setProvider(BCUtil.PROVIDER))
                    .getContentStream();
        } catch (CMSException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }
}
