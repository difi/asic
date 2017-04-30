package no.difi.asic;

import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.security.MultiMessageDigest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
class AsicReaderLayer2 implements AsicReaderLayer {

    private AsicInputStream2 asicInputStream;

    private MultiMessageDigest messageDigest;

    private Container container;

    public AsicReaderLayer2(InputStream inputStream, MultiMessageDigest messageDigest, Container container)
            throws IOException {
        this.asicInputStream = new AsicInputStream2(inputStream);
        this.messageDigest = messageDigest;
        this.container = container;
    }

    @Override
    public String next() throws IOException, AsicException {
        return asicInputStream.nextEntry();
    }

    @Override
    public void close() throws IOException {
        asicInputStream.close();
    }
}
