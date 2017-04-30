package no.difi.asic.security;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.lang.AsicException;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
public class MultiMessageDigestOutputStream extends FilterOutputStream {

    private MultiMessageDigest multiMessageDigest;

    public MultiMessageDigestOutputStream(OutputStream out, MultiMessageDigest multiMessageDigest) {
        super(out);
        this.multiMessageDigest = multiMessageDigest;
    }

    public MultiMessageDigestOutputStream(OutputStream out, MessageDigestAlgorithm... messageDigestAlgorithms)
            throws AsicException {
        this(out, new MultiMessageDigest(messageDigestAlgorithms));
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        multiMessageDigest.update((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        multiMessageDigest.update(b, off, len);
    }
}
