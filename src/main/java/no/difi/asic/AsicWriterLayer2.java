package no.difi.asic;

import no.difi.asic.api.AsicWriterLayer;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

/**
 * @author erlend
 */
class AsicWriterLayer2 implements AsicWriterLayer {

    private AsicOutputStream2 asicOutputStream;

    private MessageDigest messageDigest;

    private Container container;

    public AsicWriterLayer2(AsicOutputStream2 asicOutputStream, MessageDigest messageDigest, Container container) {
        this.asicOutputStream = asicOutputStream;
        this.messageDigest = messageDigest;
        this.container = container;
    }

    public OutputStream addContent(final String filename, final MimeType mimeType) throws IOException, AsicExcepion {
        asicOutputStream.nextEntry(filename);

        messageDigest.reset();
        OutputStream outputStream = new DigestOutputStream(asicOutputStream, messageDigest);

        return new FilterOutputStream(outputStream) {
            @Override
            public void close() throws IOException {
                asicOutputStream.closeEntry();

                container.add(new DataObject(filename, mimeType, messageDigest.digest()));
            }
        };
    }
}
