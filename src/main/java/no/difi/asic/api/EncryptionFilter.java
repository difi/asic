package no.difi.asic.api;

import no.difi.asic.lang.AsicExcepion;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public interface EncryptionFilter {

    OutputStream createFilter(OutputStream outputStream, Enum<?> algorithm, X509Certificate certificate)
            throws IOException, AsicExcepion;

}
