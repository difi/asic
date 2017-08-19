package no.difi.asic.api;

import com.google.common.io.ByteStreams;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.MimeType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
public interface AsicWriter extends Closeable {

    default void add(File file, MimeType mimeType) throws IOException {
        add(file.toPath(), mimeType);
    }

    default void add(File file, String filename, MimeType mimeType) throws IOException {
        add(file.toPath(), filename, mimeType);
    }

    default void add(Path path, MimeType mimeType) throws IOException {
        add(path, path.toFile().getName(), mimeType);
    }

    default void add(Path path, String filename, MimeType mimeType) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            add(inputStream, filename, mimeType);
        }
    }

    default void add(InputStream inputStream, String filename, MimeType mimeType) throws IOException {
        try (OutputStream outputStream = add(filename, mimeType)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }

    OutputStream add(String filename, MimeType mimeType) throws IOException;

    void setRootFile(String filename) throws AsicException;

    void sign() throws IOException;

}
