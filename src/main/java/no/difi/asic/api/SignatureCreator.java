package no.difi.asic.api;

import no.difi.asic.config.SignatureConfig;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.model.Container;

import java.io.IOException;
import java.security.KeyStore;

/**
 * @author erlend
 */
public interface SignatureCreator {

    boolean supportsRootFile();

    void create(Container container, KeyStore.PrivateKeyEntry privateKeyEntry, SignatureConfig signatureConfig)
            throws IOException, AsicExcepion;

}
