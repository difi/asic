package no.difi.asic.encryption;

import no.difi.asic.Asic;
import no.difi.asic.api.EncryptionFilter;
import no.difi.asic.builder.Properties;
import no.difi.asic.lang.AsicException;
import no.difi.asic.util.BCUtil;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.OutputEncryptor;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class CmsEncryptionEnvelopedDataFilter extends CmsEncryptionAbstractFilter {

    public static final EncryptionFilter INSTANCE = new CmsEncryptionEnvelopedDataFilter();

    /**
     * Private constructor.
     */
    private CmsEncryptionEnvelopedDataFilter() {
        // No action.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream createFilter(OutputStream outputStream, Properties properties) throws IOException {
        try {
            // Create envelope data
            CMSEnvelopedDataStreamGenerator streamGenerator = new CMSEnvelopedDataStreamGenerator();

            // Add recipients
            for (X509Certificate certificate : properties.get(Asic.ENCRYPTION_CERTIFICATES))
                streamGenerator.addRecipientInfoGenerator(
                        new JceKeyTransRecipientInfoGenerator(certificate).setProvider(BCUtil.PROVIDER));

            // Create encryptor
            OutputEncryptor outputEncryptor =
                    new JceCMSContentEncryptorBuilder(properties.get(Asic.ENCRYPTION_ALGORITHM).getOid())
                            .setProvider(BCUtil.PROVIDER)
                            .build();

            // Return OutputStream for use
            return streamGenerator.open(outputStream, outputEncryptor);
        } catch (CertificateEncodingException | CMSException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }
}
