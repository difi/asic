package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.security.MultiMessageDigest;
import no.difi.asic.security.MultiMessageDigestInputStream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
class AsicReaderLayer2 implements AsicReaderLayer {

    private AsicInputStream2 asicInputStream;

    private MultiMessageDigest messageDigest;

    private Container container;

    public InputStream current = null;

    public AsicReaderLayer2(InputStream inputStream, MultiMessageDigest messageDigest, Container container)
            throws IOException {
        this.asicInputStream = new AsicInputStream2(inputStream);
        this.messageDigest = messageDigest;
        this.container = container;
    }

    @Override
    public String next() throws IOException, AsicException {
        if (current != null)
            current.close();

        messageDigest.reset();
        InputStream inputStream = new MultiMessageDigestInputStream(asicInputStream.getContent(), messageDigest);

        current = new FilterInputStream(inputStream) {
            @Override
            public void close() throws IOException {
                current = null;

                ByteStreams.exhaust(in);

                container.update(asicInputStream.getCurrentFilename(), messageDigest);

                super.close();
            }
        };

        return asicInputStream.nextEntry();
    }

    @Override
    public InputStream getContent() throws IOException {
        return current;
    }

    @Override
    public void close() throws IOException {
        asicInputStream.close();
    }
}
