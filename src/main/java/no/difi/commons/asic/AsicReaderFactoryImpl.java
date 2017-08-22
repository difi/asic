package no.difi.commons.asic;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.api.AsicReader;
import no.difi.commons.asic.api.AsicReaderFactory;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.builder.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author erlend
 */
class AsicReaderFactoryImpl implements AsicReaderFactory {

    private Properties properties;

    protected AsicReaderFactoryImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Builder<AsicReader> openContainer(InputStream inputStream) throws IOException {
        try {
            return Builder.of(properties, properties -> {
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

    @Override
    public void verifyContainer(InputStream inputStream) throws IOException {
        try (AsicReader asicReaderInner = openContainer(inputStream).build()) {
            while (asicReaderInner.next() != null)
                asicReaderInner.writeTo(ByteStreams.nullOutputStream());
        }
    }
}
