package no.difi.asic.annotation;

import no.difi.asic.api.DecryptionFilter;
import no.difi.asic.api.EncryptionFilter;
import no.difi.asic.code.EncryptionAlgorithm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erlend
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Encryption {

    EncryptionAlgorithm algorithm();

    Class<? extends DecryptionFilter> decryptionFilter();

    Class<? extends EncryptionFilter> encryptionFilter();

    String prefix() default "";

}
