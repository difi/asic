package no.difi.asic;

import no.difi.asic.api.AsicReader;
import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.Supporting;
import no.difi.asic.builder.Properties;
import no.difi.asic.model.Container;
import no.difi.asic.security.MultiMessageDigest;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author erlend
 */
class AsicReaderImpl implements AsicReader {

    private Properties properties;

    private AsicReaderLayer asicReaderLayer;

    private Container container = new Container(Container.Mode.READER);

    private List<Supporting> handlers = new ArrayList<>();

    private Map<String, byte[]> fileCache = new HashMap<>();

    public AsicReaderImpl(Properties properties, InputStream inputStream) throws IOException {
        this.properties = properties;

        handlers.add(properties.get(AsicReader.SIGNATURE_VERIFIER));
        handlers.addAll(properties.get(AsicReader.PROCESSORS));

        MultiMessageDigest messageDigest =
                new MultiMessageDigest(properties.get(AsicReader.SIGNATURE_OBJECT_ALGORITHM));
        asicReaderLayer = new AsicReaderLayerImpl(inputStream, messageDigest, container);
    }

    public String next() throws IOException {
        String filename = asicReaderLayer.next();

        if (filename == null) {
            properties.get(AsicReader.SIGNATURE_VERIFIER)
                    .postHandler(container, fileCache);
        } else {
            Optional<Supporting> supporting = handlers.stream()
                    .filter(h -> h.supports(filename))
                    .findFirst();

            if (supporting.isPresent()) {
                supporting.get().handle(asicReaderLayer, filename, container, fileCache);
                return next();
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
