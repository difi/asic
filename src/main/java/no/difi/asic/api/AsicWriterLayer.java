package no.difi.asic.api;

import no.difi.asic.lang.AsicException;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.MimeType;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
public interface AsicWriterLayer extends Closeable {

    OutputStream addContent(final DataObject.Type type, final String filename, final MimeType mimeType)
            throws IOException, AsicException;

}
