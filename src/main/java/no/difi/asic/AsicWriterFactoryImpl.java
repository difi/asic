package no.difi.asic;

import no.difi.asic.api.AsicWriter;
import no.difi.asic.api.AsicWriterFactory;
import no.difi.asic.builder.Builder;
import no.difi.asic.builder.Properties;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
class AsicWriterFactoryImpl implements AsicWriterFactory {

    protected Properties properties;

    protected AsicWriterFactoryImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Builder<AsicWriter> newContainer(Path path) throws IOException {
        return newContainer(Files.newOutputStream(path), true);
    }

    @Override
    public Builder<AsicWriter> newContainer(OutputStream outputStream) throws IOException {
        return newContainer(outputStream, false);
    }

    private Builder<AsicWriter> newContainer(final OutputStream outputStream, final boolean closeStreamOnClose)
            throws IOException {
        return new Builder<>(properties, properties -> {
            try {
                return new AsicWriterImpl(properties, outputStream, closeStreamOnClose);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
