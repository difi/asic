package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.FileCache;

import java.io.IOException;

/**
 * @author erlend
 */
public interface Supporting {

    boolean supports(String filename);

    void handle(AsicReaderLayer asicReaderLayer, String filename, Container container, FileCache fileCache,
                Properties properties) throws IOException;

}
