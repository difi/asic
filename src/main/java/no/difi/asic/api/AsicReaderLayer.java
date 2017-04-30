package no.difi.asic.api;

import no.difi.asic.lang.AsicException;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author erlend
 */
public interface AsicReaderLayer extends Closeable {

    String next() throws IOException, AsicException;

}
