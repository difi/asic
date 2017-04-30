package no.difi.asic.api;

import no.difi.asic.lang.AsicException;

import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public interface AsicWriterBuilder<T> {

    /**
     * Adds certificate for encryption.
     *
     * @param certificate Provided certificate
     * @return Builder instance.
     */
    AsicWriterBuilder<T> encryptFor(X509Certificate certificate);

    /**
     * Adds certificate for signing.
     *
     * @param privateKeyEntry Proviced certificate
     * @return Builder instance.
     */
    AsicWriterBuilder<T> signBy(KeyStore.PrivateKeyEntry privateKeyEntry);

    /**
     * Returns the wanted instances.
     *
     * @return Actual instance.
     * @throws AsicException Thrown in case of invalid states.
     */
    T build() throws IOException, AsicException;

}
