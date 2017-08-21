package no.difi.commons.asic.api;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public interface AsicReaderLayer extends Closeable {

    String next() throws IOException;

    InputStream getContent() throws IOException;

}
