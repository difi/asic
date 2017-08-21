package no.difi.commons.asic.encryption;

import no.difi.commons.asic.Asic;
import no.difi.commons.asic.api.DecryptionFilter;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.util.BCUtil;
import org.bouncycastle.cms.CMSAuthenticatedDataParser;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransAuthenticatedRecipient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class CmsDecryptionAuthDataFilter extends CmsDecryptionAbstractFilter {

    public static final DecryptionFilter INSTANCE = new CmsDecryptionAuthDataFilter();

    /**
     * Private constructor.
     */
    private CmsDecryptionAuthDataFilter() {
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
            CMSAuthenticatedDataParser parser = new CMSAuthenticatedDataParser(inputStream);

            /*
            // Verify valid encryption algorithm is used.
            if (properties.get(Asic.DECRYPTION_VERIFY_ALGORITHM) && properties.get(Asic.DECRYPTION_ALGORITHM).stream()
                    .map(EncryptionAlgorithm::getOid)
                    .map(ASN1ObjectIdentifier::getId)
                    .noneMatch(parser.getMacAlgorithm()::equals))
                throw new AsicException(String.format("Invalid encryption '%s' was used.", parser.getMacAlgOID()));
                */

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
            return recipient.getContentStream(new JceKeyTransAuthenticatedRecipient(privateKey)
                    .setProvider(BCUtil.PROVIDER))
                    .getContentStream();
        } catch (CMSException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }
}
