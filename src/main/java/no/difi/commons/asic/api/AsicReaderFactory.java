package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Builder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public interface AsicReaderFactory {

    Builder<AsicReader, IOException> openContainer(InputStream inputStream);

    void verifyContainer(InputStream inputStream) throws IOException;
}
