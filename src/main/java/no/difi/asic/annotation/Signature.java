package no.difi.asic.annotation;

import no.difi.asic.api.SignatureCreator;
import no.difi.asic.api.SignatureVerifier;
import no.difi.asic.code.MessageDigestAlgorithm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erlend
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Signature {

    MessageDigestAlgorithm[] dataObjectAlgorithm();

    MessageDigestAlgorithm signatureAlgorithm();

    Class<? extends SignatureCreator> signatureCreator();

    Class<? extends SignatureVerifier> signatureVerifier();

}
