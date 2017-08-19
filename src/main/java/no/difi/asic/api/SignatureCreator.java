package no.difi.asic.api;

import no.difi.asic.builder.Properties;
import no.difi.asic.model.Container;

import java.io.IOException;

/**
 * @author erlend
 */
public interface SignatureCreator {

    default boolean supportsRootFile() {
        return false;
    }

    void create(AsicWriterLayer asicWriterLayer, Container container, Properties properties)
            throws IOException;

}
