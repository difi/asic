package no.difi.asic;

import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.Supporting;
import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.security.MultiMessageDigest;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erlend
 */
public class AsicReader2 implements Closeable {

    private ConfigurationWrapper configuration;

    /**
     * Certificates used for decryption.
     */
    protected List<KeyStore.PrivateKeyEntry> keyEntries = new ArrayList<>();

    private AsicReaderLayer asicReaderLayer;

    private Container container = new Container(Container.Mode.READER);

    private List<Supporting> handlers = new ArrayList<>();

    public AsicReader2(InputStream inputStream, ConfigurationWrapper configuration) throws IOException, AsicException {
        this.configuration = configuration;

        handlers.add(configuration.getSignature().getSignatureVerifier());

        MultiMessageDigest messageDigest =
                new MultiMessageDigest(configuration.getSignature().getDataObjectAlgorithm());
        asicReaderLayer = new AsicReaderLayer2(inputStream, messageDigest, container);
    }

    public String next() throws IOException, AsicException {
        String filename = asicReaderLayer.next();

        if (filename != null) {
            for (Supporting supporting : handlers) {
                if (supporting.supports(filename)) {
                    supporting.handle(asicReaderLayer, filename, container);
                    return next();
                }
            }
        }

        return filename;
    }

    public InputStream getContent() throws IOException {
        return asicReaderLayer.getContent();
    }

    @Override
    public void close() throws IOException {
        asicReaderLayer.close();
    }
}
