package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.api.AsicReader;
import no.difi.asic.builder.Builder;
import no.difi.asic.builder.Properties;
import no.difi.asic.code.MessageDigestAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author erlend
 */
public class AsicReaderFactory {

    private Properties properties;

    public static Builder<AsicReaderFactory> builder() {
        return new Builder<>(AsicReaderFactory::new);
    }

    public static Builder<AsicReaderFactory> legacy() {
        return builder()
                .set(AsicReader.SIGNATURE_ALGORITHM, MessageDigestAlgorithm.SHA1);
    }

    protected AsicReaderFactory(Properties properties) {
        this.properties = properties;
    }

    public Builder<AsicReader> openContainer(InputStream inputStream) throws IOException {
        try {
            return new Builder<>(properties, properties -> {
                try {
                    return new AsicReaderImpl(properties, inputStream);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    public void verifyContainer(InputStream inputStream) throws IOException {
        try (AsicReader asicReaderInner = openContainer(inputStream).build()) {
            while (asicReaderInner.next() != null)
                asicReaderInner.writeTo(ByteStreams.nullOutputStream());
        }
    }
}
