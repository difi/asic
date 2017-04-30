package no.difi.asic.annotation;

import no.difi.asic.api.ReaderProcessor;
import no.difi.asic.api.WriterProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erlend
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Processor {

    State state();

    Class<? extends ReaderProcessor> reader();

    Class<? extends WriterProcessor> writer();

    enum State {
        INITIAL,
        BEFORE_SIGNATURE,
        AFTER_SIGNATURE
    }
}
