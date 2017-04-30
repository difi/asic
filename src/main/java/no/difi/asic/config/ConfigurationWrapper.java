package no.difi.asic.config;

import no.difi.asic.annotation.Encryption;
import no.difi.asic.annotation.Processor;
import no.difi.asic.annotation.Processors;
import no.difi.asic.annotation.Signature;
import no.difi.asic.api.AsicWriterLayer;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.util.EnumUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author erlend
 */
public class ConfigurationWrapper {

    private EncryptionConfig encryptionConfig;

    private SignatureConfig signatureConfig;

    private List<ProcessorConfig> processorConfigs;

    public ConfigurationWrapper(Enum configuration) throws AsicException {
        Field field = EnumUtil.getField(configuration);

        signatureConfig = new SignatureConfig(field.getAnnotation(Signature.class));

        if (field.isAnnotationPresent(Encryption.class))
            encryptionConfig = new EncryptionConfig(field.getAnnotation(Encryption.class));

        if (field.isAnnotationPresent(Processor.class)) {
            processorConfigs = Collections.singletonList(new ProcessorConfig(field.getAnnotation(Processor.class)));
        } else if (field.isAnnotationPresent(Processors.class)) {
            processorConfigs = new ArrayList<>();
            for (Processor processor : field.getAnnotation(Processors.class).value())
                processorConfigs.add(new ProcessorConfig(processor));
        } else {
            processorConfigs = Collections.emptyList();
        }
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

    public void process(Processor.State state, AsicWriterLayer asicWriterLayer, Container container)
            throws IOException, AsicException {
        for (ProcessorConfig processorConfig : processorConfigs)
            if (processorConfig.getState().equals(state))
                processorConfig.getWriter().perform(asicWriterLayer, container);
    }
}
