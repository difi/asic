package no.difi.asic;

import no.difi.asic.api.AsicReaderBuilder;
import no.difi.asic.lang.AsicException;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author erlend
 */
class AsicReaderBuilder2 implements AsicReaderBuilder<AsicReader2> {

    protected AsicReaderFactory2 asicReaderFactory;

    protected InputStream inputStream;

    /**
     * Certificates used for decryption.
     */
    protected List<KeyStore.PrivateKeyEntry> keyEntries = new ArrayList<>();

    protected AsicReaderBuilder2() {
        // No action.
    }

    @Override
    public AsicReaderBuilder<AsicReader2> decryptWith(KeyStore.PrivateKeyEntry privateKeyEntry) {
        keyEntries.add(privateKeyEntry);
        return this;
    }

    @Override
    public AsicReader2 build() throws IOException, AsicException {
        AsicReader2 asicReader = new AsicReader2(inputStream, asicReaderFactory.configuration);
        asicReader.keyEntries = keyEntries.isEmpty() ?
                asicReaderFactory.keyEntries : Collections.unmodifiableList(keyEntries);

        return asicReader;
    }
}
