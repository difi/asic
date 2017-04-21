package no.difi.asic.lang;

/**
 * @author erlend
 */
public class AsicExcepion extends Exception {

    public AsicExcepion(String message) {
        super(message);
    }

    public AsicExcepion(String message, Throwable cause) {
        super(message, cause);
    }
}
