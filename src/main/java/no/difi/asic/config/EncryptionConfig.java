package no.difi.asic.config;

import no.difi.asic.annotation.Encryption;

/**
 * @author erlend
 */
public class EncryptionConfig {

    private Encryption encryption;

    EncryptionConfig(Encryption encryption) {
        this.encryption = encryption;
    }
}
