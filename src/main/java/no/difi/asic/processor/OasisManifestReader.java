package no.difi.asic.processor;

import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.ReaderProcessor;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.util.MimeTypes;

import java.io.IOException;

/**
 * @author erlend
 */
public class OasisManifestReader extends OasisManifestCommons implements ReaderProcessor {

    @Override
    public boolean supports(String filename) {
        return FILENAME.equals(filename);
    }

    @Override
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container) throws IOException {
        container.update(filename, DataObject.Type.METADATA, MimeTypes.XML);
    }
}
