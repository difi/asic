package no.difi.asic.api;

import no.difi.asic.annotation.Processor;
import no.difi.asic.model.Container;

import java.io.IOException;

/**
 * @author erlend
 */
public interface WriterProcessor {

    default Processor.State getState() {
        return this.getClass().getAnnotation(Processor.class).value();
    }

    void perform(AsicWriterLayer asicWriterLayer, Container container) throws IOException;

}
