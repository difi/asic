package no.difi.asic.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Simple wrapper of an OutputStream making sure the close method on the encapsulated OutputStream is never called.
 *
 * @author erlend
 */
public class UnclosableOutputStream extends OutputStream {

    private OutputStream outputStream;

    public UnclosableOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        // No action.
    }
}
