package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Properties;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public interface DecryptionFilter {

    boolean isEncrypted(String filename);

    InputStream createFilter(InputStream inputStream, Properties properties) throws IOException;

}
