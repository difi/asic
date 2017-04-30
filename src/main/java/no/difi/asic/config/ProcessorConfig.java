package no.difi.asic.config;

import no.difi.asic.annotation.Processor;
import no.difi.asic.api.ReaderProcessor;
import no.difi.asic.api.WriterProcessor;
import no.difi.asic.lang.AsicException;

/**
 * @author erlend
 */
public class ProcessorConfig {

    private Processor.State state;

    private ReaderProcessor reader;

    private WriterProcessor writer;

    public ProcessorConfig(Processor processor) throws AsicException {
        this.state = processor.state();

        try {
            reader = processor.reader().newInstance();
            writer = processor.writer().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AsicException("Unable to initiate signature implementation.", e);
        }
    }

    public Processor.State getState() {
        return state;
    }

    public ReaderProcessor getReader() {
        return reader;
    }

    public WriterProcessor getWriter() {
        return writer;
    }
}
