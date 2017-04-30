package no.difi.asic;

import no.difi.asic.api.AsicReaderBuilder;
import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicException;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Collections;
import java.util.List;

/**
 * @author erlend
 */
public class AsicReaderFactory2 {

    protected ConfigurationWrapper configuration;

    protected List<KeyStore.PrivateKeyEntry> keyEntries;

    public static AsicReaderBuilder<AsicReaderFactory2> newFactory() {
        return newFactory(Configuration.LEGACY);
    }

    public static AsicReaderBuilder<AsicReaderFactory2> newFactory(Enum configuration) {
        return new AsicReaderFactoryBuilder2(configuration);
    }

    protected AsicReaderFactory2(AsicReaderFactoryBuilder2 builder) throws AsicException {
        this.configuration = new ConfigurationWrapper(builder.configuration);
        this.keyEntries = Collections.unmodifiableList(builder.keyEntries);
    }

    protected AsicReaderBuilder<AsicReader2> open(InputStream inputStream) {
        AsicReaderBuilder2 asicReaderBuilder = new AsicReaderBuilder2();
        asicReaderBuilder.asicReaderFactory = this;
        asicReaderBuilder.inputStream = inputStream;

        return asicReaderBuilder;
    }
}
