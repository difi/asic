package no.difi.asic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erlend
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Processor {

    State value();

    enum State {
        INITIAL,
        BEFORE_SIGNATURE,
        AFTER_SIGNATURE
    }
}
