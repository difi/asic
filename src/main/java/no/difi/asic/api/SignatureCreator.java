package no.difi.asic.api;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.model.Container;

import java.io.IOException;
import java.security.KeyStore;
import java.util.List;

/**
 * @author erlend
 */
public interface SignatureCreator {

    boolean supportsRootFile();

    /*
    default boolean supportsRootFile() {
        return false;
    }
    */

    void create(AsicWriterLayer asicWriterLayer, Container container, List<KeyStore.PrivateKeyEntry> keyEntries,
                MessageDigestAlgorithm objectAlgorithm, MessageDigestAlgorithm signatureAlgorithm)
            throws IOException;

}
