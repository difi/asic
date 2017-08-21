package no.difi.commons.asic.encryption;

import com.google.common.io.BaseEncoding;
import no.difi.commons.asic.api.DecryptionFilter;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.lang.AsicException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;

/**
 * @author erlend
 */
public class CmsDecryptionDetectorFilter extends CmsDecryptionAbstractFilter {

    public static final DecryptionFilter INSTANCE = new CmsDecryptionDetectorFilter();

    private static final byte[] AUTH_DATA_START = new byte[]{
            0x30, (byte) 0x80, 0x06, 0x0B, 0x2A, (byte) 0x86, 0x48, (byte) 0x86,
            (byte) 0xF7, 0x0D, 0x01, 0x09, 0x10, 0x01, 0x02, (byte) 0xA0
    };

    private static final byte[] ENVELOPED_DATA_START = new byte[]{
            0x30, (byte) 0x80, 0x06, 0x09, 0x2A, (byte) 0x86, 0x48, (byte) 0x86,
            (byte) 0xF7, 0x0D, 0x01, 0x07, 0x03, (byte) 0xA0, (byte) 0x80, 0x30
    };

    /**
     * Private constructor.
     */
    private CmsDecryptionDetectorFilter() {
        // No action.
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public InputStream createFilter(InputStream inputStream, Properties properties) throws IOException {
        // Read start of encrypted file.
        byte[] start = new byte[16];
        inputStream.read(start);

        // Combine data read above with reminding of stream.
        InputStream fixedInputStream = new SequenceInputStream(
                new ByteArrayInputStream(start),
                inputStream
        );

        // Check for EnvelopedData.
        if (Arrays.equals(start, ENVELOPED_DATA_START))
            return CmsDecryptionEnvelopedDataFilter.INSTANCE.createFilter(fixedInputStream, properties);

            // Check for AuthenticatedData.
        else if (Arrays.equals(start, AUTH_DATA_START))
            return CmsDecryptionAuthDataFilter.INSTANCE.createFilter(fixedInputStream, properties);

        // Throw exception if encryption used is not recognized.
        throw new AsicException(String.format("Unable to recognize encryption starting with '%s'.",
                BaseEncoding.base16().encode(start)));
    }
}
