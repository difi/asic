package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.FileCache;

import java.io.IOException;

/**
 * @author erlend
 */
public interface SignatureVerifier extends Supporting {

    default void postHandler(Container container, FileCache fileCache, Properties properties)
            throws IOException {
        // No action.
    }
}
