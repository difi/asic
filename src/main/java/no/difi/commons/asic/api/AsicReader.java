package no.difi.commons.asic.api;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.model.Container;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
public interface AsicReader extends Closeable {

    String next() throws IOException;

    InputStream getContent() throws IOException;

    Container getContainer() throws IOException;

    default void writeTo(Path path) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            writeTo(outputStream);
        }
    }

    default void writeTo(OutputStream outputStream) throws IOException {
        try (InputStream inputStream = getContent()) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }
}
