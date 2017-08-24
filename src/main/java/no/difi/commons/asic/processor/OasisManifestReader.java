package no.difi.commons.asic.processor;

import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.api.ReaderProcessor;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.FileCache;
import no.difi.commons.asic.model.MimeType;

import java.io.IOException;

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
        container.update(filename, DataObject.Type.METADATA, MimeType.APPLICATION_XML);

        // No reason to use further resources on verification of content.
    }
}
