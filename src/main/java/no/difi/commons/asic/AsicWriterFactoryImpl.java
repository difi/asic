package no.difi.commons.asic;

import no.difi.commons.asic.api.AsicWriter;
import no.difi.commons.asic.api.AsicWriterFactory;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.builder.Properties;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
class AsicWriterFactoryImpl implements AsicWriterFactory {

    protected final Properties properties;

    protected AsicWriterFactoryImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Builder<AsicWriter, IOException> newContainer(Path path) throws IOException {
        return newContainer(Files.newOutputStream(path), true);
    }

    @Override
    public Builder<AsicWriter, IOException> newContainer(OutputStream outputStream) {
        return newContainer(outputStream, false);
    }

    private Builder<AsicWriter, IOException> newContainer(final OutputStream outputStream,
                                                          final boolean closeStreamOnClose) {
        return Builder.<AsicWriter, IOException>of(p -> new AsicWriterImpl(p, outputStream, closeStreamOnClose))
                .set(properties);
    }
}
