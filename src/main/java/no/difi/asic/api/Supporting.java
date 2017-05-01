package no.difi.asic.api;

import no.difi.asic.model.Container;

import java.io.IOException;

/**
 * @author erlend
 */
public interface Supporting {

    boolean supports(String filename);

    void handle(AsicReaderLayer asicReaderLayer, String filename, Container container)
            throws IOException;

}
