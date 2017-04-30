package no.difi.asic;

import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.MimeType;
import no.difi.asic.security.MultiMessageDigest;
import no.difi.asic.security.MultiMessageDigestOutputStream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
class AsicWriterLayer implements no.difi.asic.api.AsicWriterLayer {

    private AsicOutputStream asicOutputStream;

    private MultiMessageDigest messageDigest;

    private Container container;

    public AsicWriterLayer(OutputStream outputStream, MultiMessageDigest messageDigest, Container container)
            throws IOException {
        this.asicOutputStream = new AsicOutputStream(outputStream);
        this.messageDigest = messageDigest;
        this.container = container;
    }

    @Override
    public OutputStream addContent(DataObject.Type type, final String filename, MimeType mimeType)
            throws IOException, AsicException {
        container.update(filename, type, mimeType);

        asicOutputStream.nextEntry(filename);

        messageDigest.reset();
        OutputStream outputStream = new MultiMessageDigestOutputStream(asicOutputStream, messageDigest);

        return new FilterOutputStream(outputStream) {
            @Override
            public void close() throws IOException {
                asicOutputStream.closeEntry();

                container.update(filename, messageDigest);
            }
        };
    }

    @Override
    public void close() throws IOException {
        asicOutputStream.close();
    }
}
