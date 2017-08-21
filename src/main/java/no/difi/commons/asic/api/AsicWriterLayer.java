package no.difi.commons.asic.api;

import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.MimeType;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
public interface AsicWriterLayer extends Closeable {

    OutputStream addContent(final DataObject.Type type, final String filename, final MimeType mimeType)
            throws IOException;

}
