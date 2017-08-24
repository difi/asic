package no.difi.commons.asic;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.api.AsicReader;
import no.difi.commons.asic.api.AsicReaderFactory;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.builder.Properties;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
class AsicReaderFactoryImpl implements AsicReaderFactory {

    private final Properties properties;

    protected AsicReaderFactoryImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Builder<AsicReader, IOException> openContainer(InputStream inputStream) {
        return Builder.<AsicReader, IOException>of(p -> new AsicReaderImpl(properties, inputStream))
                .set(properties);
    }

    @Override
    public void verifyContainer(InputStream inputStream) throws IOException {
        try (AsicReader asicReaderInner = openContainer(inputStream).build()) {
            while (asicReaderInner.next() != null)
                asicReaderInner.writeTo(ByteStreams.nullOutputStream());
        }
    }
}
