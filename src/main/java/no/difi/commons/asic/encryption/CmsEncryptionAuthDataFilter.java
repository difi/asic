package no.difi.commons.asic.encryption;

import no.difi.commons.asic.Asic;
import no.difi.commons.asic.api.EncryptionFilter;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.util.BCUtil;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSAuthenticatedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceCMSMacCalculatorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.MacCalculator;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class CmsEncryptionAuthDataFilter extends CmsEncryptionAbstractFilter {

    public static final EncryptionFilter INSTANCE = new CmsEncryptionAuthDataFilter();

    /**
     * Private constructor.
     */
    private CmsEncryptionAuthDataFilter() {
        // No action.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream createFilter(OutputStream outputStream, Properties properties) throws IOException {
        try {
            // Create authenticated data
            CMSAuthenticatedDataStreamGenerator streamGenerator = new CMSAuthenticatedDataStreamGenerator();

            // Add recipients
            for (X509Certificate certificate : properties.get(Asic.ENCRYPTION_CERTIFICATES))
                streamGenerator.addRecipientInfoGenerator(
                        new JceKeyTransRecipientInfoGenerator(certificate).setProvider(BCUtil.PROVIDER));

            // Create calculator
            // properties.get(Asic.ENCRYPTION_ALGORITHM).getOid();
            MacCalculator macCalculator =
                    new JceCMSMacCalculatorBuilder(CMSAlgorithm.DES_EDE3_CBC)
                            .setProvider(BCUtil.PROVIDER)
                            .build();

            // Return OutputStream for use
            return streamGenerator.open(outputStream, macCalculator);
        } catch (CertificateEncodingException | CMSException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }
}
