package no.difi.asic.processor;

import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.ReaderProcessor;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
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
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container
            , Map<String, byte[]> fileCache) throws IOException {
        container.update(filename, DataObject.Type.METADATA, MimeTypes.XML);
    }
}
