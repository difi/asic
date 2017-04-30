package no.difi.asic.api;

import no.difi.asic.lang.AsicException;

import java.io.IOException;
import java.security.KeyStore;

/**
 * @author erlend
 */
public interface AsicReaderBuilder<T> {

    AsicReaderBuilder<T> decryptWith(KeyStore.PrivateKeyEntry privateKeyEntry);

    /**
     * Returns the wanted instances.
     *
     * @return Actual instance.
     * @throws AsicException Thrown in case of invalid states.
     */
    T build() throws IOException, AsicException;

}
