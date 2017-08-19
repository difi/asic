package no.difi.asic.api;

import no.difi.asic.model.Container;

import java.io.IOException;
import java.util.Map;

/**
 * @author erlend
 */
public interface SignatureVerifier extends Supporting {

    default void postHandler(Container container, Map<String, byte[]> fileCache) throws IOException {
        // No action.
    }
}
