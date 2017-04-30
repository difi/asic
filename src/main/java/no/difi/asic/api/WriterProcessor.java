package no.difi.asic.api;

import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;

import java.io.IOException;

/**
 * @author erlend
 */
public interface WriterProcessor {

    void perform(AsicWriterLayer asicWriterLayer, Container container) throws IOException, AsicException;

}
