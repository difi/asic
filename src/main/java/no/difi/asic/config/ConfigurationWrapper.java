package no.difi.asic.config;

import no.difi.asic.annotation.Encryption;
import no.difi.asic.annotation.Signature;
import no.difi.asic.lang.AsicException;
import no.difi.asic.util.EnumUtil;

import java.lang.reflect.Field;

/**
 * @author erlend
 */
public class ConfigurationWrapper {

    private EncryptionConfig encryptionConfig;

    private SignatureConfig signatureConfig;

    public ConfigurationWrapper(Enum configuration) throws AsicException {
        Field field = EnumUtil.getField(configuration);

        signatureConfig = new SignatureConfig(field.getAnnotation(Signature.class));

        if (field.isAnnotationPresent(Encryption.class))
            encryptionConfig = new EncryptionConfig(field.getAnnotation(Encryption.class));
    }

    public EncryptionConfig getEncryption() {
        return encryptionConfig;
    }

    public SignatureConfig getSignature() {
        return signatureConfig;
    }

    public boolean supportsEncryption() {
        return encryptionConfig != null;
    }
}
