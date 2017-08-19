package no.difi.asic;

import no.difi.asic.api.AsicWriter;
import no.difi.asic.builder.Builder;
import no.difi.asic.builder.Properties;
import no.difi.asic.code.MessageDigestAlgorithm;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
public class AsicWriterFactory {

    protected Properties properties;

    public static Builder<AsicWriterFactory> builder() {
        return new Builder<>(AsicWriterFactory::new);
    }

    public static Builder<AsicWriterFactory> legacy() {
        return builder()
                .set(AsicWriter.SIGNATURE_ALGORITHM, MessageDigestAlgorithm.SHA1);
    }

    protected AsicWriterFactory(Properties properties) {
        this.properties = properties;
    }

    /**
     * Factory method creating a new AsicWriterOld, which will create an ASiC archive in the supplied
     * directory with the supplied file name
     *
     * @param outputDir the directory in which the archive will be created.
     * @param filename  the name of the archive.
     * @return an instance of AsicWriterImpl
     */
    public Builder<AsicWriter> newContainer(File outputDir, String filename) throws IOException {
        return newContainer(new File(outputDir, filename));
    }

    /**
     * Creates a new AsicWriterImpl, which will create an ASiC archive in the supplied file.
     *
     * @param file the file reference to the archive.
     * @return an instance of AsicWriterImpl
     */
    public Builder<AsicWriter> newContainer(File file) throws IOException {
        return newContainer(file.toPath());
    }

    /**
     * @see #newContainer(File)
     */
    public Builder<AsicWriter> newContainer(Path path) throws IOException {
        return newContainer(Files.newOutputStream(path), true);
    }

    /**
     * Creates a new AsicWriterImpl, which will createFilter the container contents to the supplied output stream.
     *
     * @param outputStream stream into which the archive will be written.
     * @return an instance of AsicWriterImpl
     */
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
