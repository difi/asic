package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Builder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * @author erlend
 */
public interface AsicWriterFactory {

    /**
     * Factory method creating a new AsicWriterOld, which will create an ASiC archive in the supplied
     * directory with the supplied file name
     *
     * @param outputDir the directory in which the archive will be created.
     * @param filename  the name of the archive.
     * @return an instance of AsicWriterImpl
     */
    default Builder<AsicWriter, IOException> newContainer(File outputDir, String filename) throws IOException {
        return newContainer(new File(outputDir, filename));
    }

    /**
     * Creates a new AsicWriterImpl, which will create an ASiC archive in the supplied file.
     *
     * @param file the file reference to the archive.
     * @return an instance of AsicWriterImpl
     */
    default Builder<AsicWriter, IOException> newContainer(File file) throws IOException {
        return newContainer(file.toPath());
    }

    /**
     * @see #newContainer(File)
     */
    Builder<AsicWriter, IOException> newContainer(Path path) throws IOException;

    /**
     * Creates a new AsicWriterImpl, which will createFilter the container contents to the supplied output stream.
     *
     * @param outputStream stream into which the archive will be written.
     * @return an instance of AsicWriterImpl
     */
    Builder<AsicWriter, IOException> newContainer(OutputStream outputStream);
}
