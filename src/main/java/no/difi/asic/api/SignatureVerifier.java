package no.difi.asic.api;

import no.difi.asic.builder.Properties;
import no.difi.asic.model.Container;
import no.difi.asic.model.FileCache;

import java.io.IOException;
import java.util.Map;

/**
 * @author erlend
 */
public interface SignatureVerifier extends Supporting {

    default void postHandler(Container container, FileCache fileCache, Properties properties)
            throws IOException {
        // No action.
    }
}
