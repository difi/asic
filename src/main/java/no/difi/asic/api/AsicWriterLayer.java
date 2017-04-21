package no.difi.asic.api;

import no.difi.asic.MimeType;
import no.difi.asic.lang.AsicExcepion;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
public interface AsicWriterLayer {

    OutputStream addContent(final String filename, final MimeType mimeType) throws IOException, AsicExcepion;

}
