package no.difi.commons.asic;

import no.difi.commons.asic.api.AsicReader;
import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.api.DecryptionFilter;
import no.difi.commons.asic.api.Supporting;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.FileCache;
import no.difi.commons.asic.security.MultiMessageDigest;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author erlend
 */
class AsicReaderImpl implements AsicReader {

    private no.difi.commons.asic.builder.Properties properties;

    private AsicReaderLayer asicReaderLayer;

    private Container container = new Container(Container.Mode.READER);

    private List<Supporting> handlers = new ArrayList<>();

    private List<DecryptionFilter> decryptionFilters;

    private FileCache fileCache = new FileCache();

    public AsicReaderImpl(no.difi.commons.asic.builder.Properties properties, InputStream inputStream) throws IOException {
        this.properties = properties;

        handlers.add(properties.get(Asic.SIGNATURE_VERIFIER));
        handlers.addAll(properties.get(Asic.READER_PROCESSORS));

        decryptionFilters = properties.get(Asic.DECRYPTION_FILTER);

        MultiMessageDigest messageDigest =
                new MultiMessageDigest(properties.get(Asic.SIGNATURE_OBJECT_ALGORITHM));
        asicReaderLayer = new AsicReaderLayerImpl(inputStream, messageDigest, container);
    }

    public String next() throws IOException {
        String filename = asicReaderLayer.next();

        if (filename == null) {
            properties.get(Asic.SIGNATURE_VERIFIER)
                    .postHandler(container, fileCache, properties);

            // TODO Verify all files in container.
        } else {
            // TODO Encrypted content?

            Optional<Supporting> supporting = handlers.stream()
                    .filter(h -> h.supports(filename))
                    .findFirst();

            if (supporting.isPresent()) {
                supporting.get().handle(asicReaderLayer, filename, container, fileCache, properties);
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
