package no.difi.commons.asic;

import no.difi.commons.asic.api.AsicReader;
import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.api.DecryptionFilter;
import no.difi.commons.asic.api.Supporting;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.FileCache;
import no.difi.commons.asic.security.MultiMessageDigest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author erlend
 */
class AsicReaderImpl implements AsicReader {

    private Properties properties;

    private AsicReaderLayer asicReaderLayer;

    private Container container = new Container(Container.Mode.READER);

    private List<Supporting> handlers = new ArrayList<>();

    private List<DecryptionFilter> decryptionFilters;

    private FileCache fileCache = new FileCache();

    private boolean finished = false;

    private DecryptionFilter currentFilter;

    public AsicReaderImpl(Properties properties, InputStream inputStream)
            throws IOException {
        this.properties = properties;

        // Setting up handlers
        handlers.add(properties.get(Asic.SIGNATURE_VERIFIER));
        handlers.addAll(properties.get(Asic.READER_PROCESSORS));

        // Decryption filter
        decryptionFilters = properties.get(Asic.DECRYPTION_FILTER);

        // Create digester
        MultiMessageDigest messageDigest =
                new MultiMessageDigest(properties.get(Asic.SIGNATURE_OBJECT_ALGORITHM));

        // Creating layer
        asicReaderLayer = new AsicReaderLayerImpl(inputStream, messageDigest, container);
    }

    @Override
    public String next() throws IOException {
        String filename = asicReaderLayer.next();

        // End of file?
        if (filename == null) {
            properties.get(Asic.SIGNATURE_VERIFIER)
                    .postHandler(container, fileCache, properties);

            // TODO Verify all files in container.

            finished = true;

            return null;
        }

        // Detect metadata file
        Optional<Supporting> supporting = handlers.stream()
                .filter(h -> h.supports(filename))
                .findFirst();

        if (supporting.isPresent()) {
            supporting.get().handle(asicReaderLayer, filename, container, fileCache, properties);
            return next();
        }

        // Detect encrypted file.
        currentFilter = decryptionFilters.stream()
                .filter(f -> f.isEncrypted(filename))
                .findAny()
                .orElse(DecryptionFilter.NOOP);

        return currentFilter.parseFilename(filename);
    }

    @Override
    public InputStream getContent() throws IOException {
        if (finished)
            throw new AsicException("No more to read at the end of container.");

        return currentFilter.createFilter(asicReaderLayer.getContent(), properties);
    }

    @Override
    public Container getContainer() throws IOException {
        if (!finished)
            throw new AsicException("Reading of file must come to an end before fetching the Container object.");

        return container;
    }

    @Override
    public void close() throws IOException {
        asicReaderLayer.close();
    }
}
