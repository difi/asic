package no.difi.commons.asic.security;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public class MultiMessageDigestInputStream extends InputStream {

    private InputStream inputStream;

    private MultiMessageDigest multiMessageDigest;

    public MultiMessageDigestInputStream(InputStream in, MultiMessageDigest messageDigest) {
        this.inputStream = in;
        this.multiMessageDigest = messageDigest;
    }

    @Override
    public int read() throws IOException {
        int b = inputStream.read();
        if (b != -1)
            multiMessageDigest.update((byte) b);
        return b;
    }
}
