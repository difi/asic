package no.difi.asic;

import no.difi.asic.api.AsicWriterBuilder;
import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author erlend
 */
public class AsicWriterFactory2 {

    protected ConfigurationWrapper configuration;

    protected List<X509Certificate> certificates;

    protected List<KeyStore.PrivateKeyEntry> keyEntries;

    public static AsicWriterBuilder<AsicWriterFactory2> newFactory() throws AsicException {
        return newFactory(Configuration.LAGACY);
    }

    public static AsicWriterBuilder<AsicWriterFactory2> newFactory(Enum configuration) throws AsicException {
        return new AsicWriterFactoryBuilder2(configuration);
    }

    protected AsicWriterFactory2(AsicWriterFactoryBuilder2 builder, Enum configuration) throws AsicException {
        this.configuration = new ConfigurationWrapper(configuration);
        this.certificates = builder.certificates;
        this.keyEntries = builder.keyEntries;
    }

    /**
     * Factory method creating a new AsicWriter, which will create an ASiC archive in the supplied
     * directory with the supplied file name
     *
     * @param outputDir the directory in which the archive will be created.
     * @param filename  the name of the archive.
     * @return an instance of AsicWriter
     */
    public AsicWriterBuilder<AsicWriter2> newContainer(File outputDir, String filename) throws IOException, AsicException {
        return newContainer(new File(outputDir, filename));
    }

    /**
     * Creates a new AsicWriter, which will create an ASiC archive in the supplied file.
     *
     * @param file the file reference to the archive.
     * @return an instance of AsicWriter
     */
    public AsicWriterBuilder<AsicWriter2> newContainer(File file) throws IOException, AsicException {
        return newContainer(file.toPath());
    }

    /**
     * @see #newContainer(File)
     */
    public AsicWriterBuilder<AsicWriter2> newContainer(Path path) throws IOException, AsicException {
        return newContainer(Files.newOutputStream(path), true);
    }

    /**
     * Creates a new AsicWriter, which will createFilter the container contents to the supplied output stream.
     *
     * @param outputStream stream into which the archive will be written.
     * @return an instance of AsicWriter
     */
    public AsicWriterBuilder<AsicWriter2> newContainer(OutputStream outputStream) throws IOException, AsicException {
        return newContainer(outputStream, false);
    }

    private AsicWriterBuilder<AsicWriter2> newContainer(OutputStream outputStream, boolean closeStreamOnClose)
            throws IOException, AsicException {
        AsicWriterBuilder2 asicWriterBuilder = new AsicWriterBuilder2();
        asicWriterBuilder.asicWriterFactory2 = this;
        asicWriterBuilder.outputStream = outputStream;
        asicWriterBuilder.closeStreamOnClose = closeStreamOnClose;

        return asicWriterBuilder;
    }
}
