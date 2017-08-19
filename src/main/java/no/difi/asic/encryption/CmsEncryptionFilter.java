package no.difi.asic.encryption;

import no.difi.asic.api.EncryptionFilter;
import no.difi.asic.code.EncryptionAlgorithm;
import no.difi.asic.lang.AsicException;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.OutputEncryptor;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author erlend
 */
public class CmsEncryptionFilter extends CmsCommons implements EncryptionFilter {

    public static final EncryptionFilter INSTANCE = new CmsEncryptionFilter();

    @Override
    public OutputStream createFilter(OutputStream outputStream, EncryptionAlgorithm algorithm,
                                     List<X509Certificate> certificates)
            throws IOException {
        try {
            // Create envelope data
            CMSEnvelopedDataStreamGenerator streamGenerator = new CMSEnvelopedDataStreamGenerator();
            for (X509Certificate certificate : certificates)
                streamGenerator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(certificate));

            // Create encryptor
            OutputEncryptor outputEncryptor = new JceCMSContentEncryptorBuilder(algorithm.getOid())
                    .setProvider(PROVIDER)
                    .build();

            // Return OutputStream for use
            return streamGenerator.open(outputStream, outputEncryptor);
        } catch (CertificateEncodingException | CMSException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }
}
