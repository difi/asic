package no.difi.asic.encryption;

import no.difi.asic.api.DecryptionFilter;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

/**
 * @author erlend
 */
public class CmsDecryptionFilter extends CmsCommons implements DecryptionFilter {

    @Override
    public InputStream createFilter(InputStream inputStream, Enum<?> algorithm, PrivateKey privateKey)
            throws IOException {
        return null;
    }
}
