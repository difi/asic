package no.difi.asic.processor;

import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.ReaderProcessor;
import no.difi.asic.builder.Properties;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.FileCache;
import no.difi.asic.util.MimeTypes;

import java.io.IOException;
import java.util.Map;

/**
 * @author erlend
 */
public class OasisManifestReader extends OasisManifestCommons implements ReaderProcessor {

    public static final ReaderProcessor INSTANCE = new OasisManifestReader();

    @Override
    public boolean supports(String filename) {
        return FILENAME.equals(filename);
    }

    @Override
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container,
                       FileCache fileCache, Properties properties) throws IOException {
        container.update(filename, DataObject.Type.METADATA, MimeTypes.XML);

        // No reason to use further resources on verification of content.
    }
}
