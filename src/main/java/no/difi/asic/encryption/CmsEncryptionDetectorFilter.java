package no.difi.asic.encryption;

import no.difi.asic.api.EncryptionFilter;
import no.difi.asic.builder.Properties;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
public class CmsEncryptionDetectorFilter extends CmsEncryptionAbstractFilter {

    public static final EncryptionFilter INSTANCE = new CmsEncryptionDetectorFilter();

    /**
     * Private constructor.
     */
    private CmsEncryptionDetectorFilter() {
        // No action.
    }

    @Override
    public OutputStream createFilter(OutputStream outputStream, Properties properties) throws IOException {
        // TODO Dummy for now.
        return CmsEncryptionEnvelopedDataFilter.INSTANCE.createFilter(outputStream, properties);
    }
}
