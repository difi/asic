package no.difi.asic.lang;

/**
 * @author erlend
 */
public class AsicException extends Exception {

    public AsicException(String message) {
        super(message);
    }

    public AsicException(String message, Throwable cause) {
        super(message, cause);
    }
}
