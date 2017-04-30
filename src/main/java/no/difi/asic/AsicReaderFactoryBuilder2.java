package no.difi.asic;

import no.difi.asic.api.AsicReaderBuilder;
import no.difi.asic.lang.AsicException;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erlend
 */
class AsicReaderFactoryBuilder2 implements AsicReaderBuilder<AsicReaderFactory2> {

    /**
     * Enum provided to be used for configuration.
     */
    protected Enum configuration;

    /**
     * Certificates used for decryption.
     */
    protected List<KeyStore.PrivateKeyEntry> keyEntries = new ArrayList<>();

    protected AsicReaderFactoryBuilder2(Enum configuration) {
        this.configuration = configuration;
    }

    @Override
    public AsicReaderBuilder<AsicReaderFactory2> decryptWith(KeyStore.PrivateKeyEntry privateKeyEntry) {
        keyEntries.add(privateKeyEntry);
        return this;
    }

    @Override
    public AsicReaderFactory2 build() throws IOException, AsicException {
        return new AsicReaderFactory2(this);
    }
}
