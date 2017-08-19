package no.difi.asic.api;

import no.difi.asic.builder.Properties;
import no.difi.asic.model.Container;
import no.difi.asic.model.FileCache;

import java.io.IOException;
import java.util.Map;

/**
 * @author erlend
 */
public interface Supporting {

    boolean supports(String filename);

    void handle(AsicReaderLayer asicReaderLayer, String filename, Container container, FileCache fileCache,
                Properties properties) throws IOException;

}
