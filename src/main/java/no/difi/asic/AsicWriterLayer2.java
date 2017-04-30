package no.difi.asic;

import no.difi.asic.api.AsicWriterLayer;
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
class AsicWriterLayer2 implements AsicWriterLayer {

    private AsicOutputStream2 asicOutputStream;

    private MultiMessageDigest messageDigest;

    private Container container;

    public AsicWriterLayer2(OutputStream outputStream, MultiMessageDigest messageDigest, Container container)
            throws IOException {
        this.asicOutputStream = new AsicOutputStream2(outputStream);
        this.messageDigest = messageDigest;
        this.container = container;
    }

    @Override
    public OutputStream addContent(DataObject.Type type, String filename, MimeType mimeType)
            throws IOException, AsicException {
        final DataObject dataObject = new DataObject(type, filename, mimeType);
        container.add(dataObject);

        asicOutputStream.nextEntry(filename);

        messageDigest.reset();
        OutputStream outputStream = new MultiMessageDigestOutputStream(asicOutputStream, messageDigest);

        return new FilterOutputStream(outputStream) {
            @Override
            public void close() throws IOException {
                asicOutputStream.closeEntry();

                dataObject.getHash().update(messageDigest);
            }
        };
    }

    @Override
    public void close() throws IOException {
        asicOutputStream.close();
    }
}
