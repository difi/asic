package no.difi.asic.encryption;

import no.difi.asic.annotation.OIDValue;
import no.difi.asic.api.EncryptionFilter;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.util.EnumUtil;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OutputEncryptor;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class CmsEncryptionFilter extends CmsCommons implements EncryptionFilter {

    @Override
    public OutputStream createFilter(OutputStream outputStream, Enum<?> algorithm, X509Certificate certificate)
            throws IOException, AsicExcepion {
        try {
            ASN1ObjectIdentifier cmsAlgorithm =
                    new ASN1ObjectIdentifier(EnumUtil.getAnnotation(algorithm, OIDValue.class).value());

            // Create envelope data
            CMSEnvelopedDataStreamGenerator envelopedDataStreamGenerator = new CMSEnvelopedDataStreamGenerator();
            envelopedDataStreamGenerator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(certificate));

            // Create encryptor
            OutputEncryptor outputEncryptor = new JceCMSContentEncryptorBuilder(cmsAlgorithm)
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .build();

            // Return OutputStream for use
            return envelopedDataStreamGenerator.open(outputStream, outputEncryptor);
        } catch (CertificateEncodingException | CMSException e) {
            throw new AsicExcepion(e.getMessage(), e);
        }
    }
}
