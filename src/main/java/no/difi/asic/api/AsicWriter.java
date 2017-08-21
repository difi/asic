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

    default AsicWriter add(File file, MimeType mimeType) throws IOException {
        return add(file.toPath(), mimeType);
    }

    default AsicWriter add(File file, String filename, MimeType mimeType) throws IOException {
        return add(file.toPath(), filename, mimeType);
    }

    default AsicWriter add(Path path, MimeType mimeType) throws IOException {
        return add(path, path.toFile().getName(), mimeType);
    }

    default AsicWriter add(Path path, String filename, MimeType mimeType) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return add(inputStream, filename, mimeType);
        }
    }

    default AsicWriter add(InputStream inputStream, String filename, MimeType mimeType) throws IOException {
        try (OutputStream outputStream = add(filename, mimeType)) {
            ByteStreams.copy(inputStream, outputStream);
        }

        return this;
    }

    OutputStream add(String filename, MimeType mimeType) throws IOException;

    AsicWriter encryptNext();

    AsicWriter setRootFile(String filename) throws AsicException;

    AsicWriter sign() throws IOException;

}
