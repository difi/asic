package no.difi.asic;

import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicExcepion;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
public class AsicWriterFactory2 {

    private ConfigurationWrapper configuration;

    public static AsicWriterFactory2 newFactory() throws AsicExcepion {
        return newFactory(Configuration.LAGACY);
    }

    public static AsicWriterFactory2 newFactory(Enum configuration) throws AsicExcepion {
        return new AsicWriterFactory2(configuration);
    }

    private AsicWriterFactory2(Enum configuration) throws AsicExcepion {
        this.configuration = new ConfigurationWrapper(configuration);
    }

    /**
     * Factory method creating a new AsicWriter, which will create an ASiC archive in the supplied
     * directory with the supplied file name
     *
     * @param outputDir the directory in which the archive will be created.
     * @param filename  the name of the archive.
     * @return an instance of AsicWriter
     */
    public AsicWriter2 newContainer(File outputDir, String filename) throws IOException, AsicExcepion {
        return newContainer(new File(outputDir, filename));
    }

    /**
     * Creates a new AsicWriter, which will create an ASiC archive in the supplied file.
     *
     * @param file the file reference to the archive.
     * @return an instance of AsicWriter
     */
    public AsicWriter2 newContainer(File file) throws IOException, AsicExcepion {
        return newContainer(file.toPath());
    }

    /**
     * @see #newContainer(File)
     */
    public AsicWriter2 newContainer(Path path) throws IOException, AsicExcepion {
        return newContainer(Files.newOutputStream(path), true);
    }

    /**
     * Creates a new AsicWriter, which will createFilter the container contents to the supplied output stream.
     *
     * @param outputStream stream into which the archive will be written.
     * @return an instance of AsicWriter
     */
    public AsicWriter2 newContainer(OutputStream outputStream) throws IOException, AsicExcepion {
        return newContainer(outputStream, false);
    }

    private AsicWriter2 newContainer(OutputStream outputStream, boolean closeStreamOnClose)
            throws IOException, AsicExcepion {
        return new AsicWriter2(outputStream, configuration);
    }
}
