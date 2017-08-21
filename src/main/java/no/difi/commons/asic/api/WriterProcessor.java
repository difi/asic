package no.difi.commons.asic.api;

import no.difi.commons.asic.annotation.Processor;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.model.Container;

import java.io.IOException;

/**
 * @author erlend
 */
public interface WriterProcessor {

    default Processor.State getState() {
        return this.getClass().getAnnotation(Processor.class).value();
    }

    void perform(AsicWriterLayer asicWriterLayer, Container container, Properties properties) throws IOException;

}
