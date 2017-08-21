package no.difi.commons.asic.lang;

import java.io.IOException;

/**
 * @author erlend
 */
public class AsicException extends IOException {

    public AsicException(String message) {
        super(message);
    }

    public AsicException(String message, Throwable cause) {
        super(message, cause);
    }
}
