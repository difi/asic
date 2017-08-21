package no.difi.commons.asic;

import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.MimeType;
import no.difi.commons.asic.security.MultiMessageDigest;
import no.difi.commons.asic.security.MultiMessageDigestOutputStream;
import no.difi.commons.asic.api.AsicWriterLayer;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
class AsicWriterLayerImpl implements AsicWriterLayer {

    private AsicOutputStream asicOutputStream;

    private MultiMessageDigest messageDigest;

    private Container container;

    public AsicWriterLayerImpl(OutputStream outputStream, MultiMessageDigest messageDigest, Container container)
            throws IOException {
        this.asicOutputStream = new AsicOutputStream(outputStream);
        this.messageDigest = messageDigest;
        this.container = container;
    }

    @Override
    public OutputStream addContent(DataObject.Type type, final String filename, MimeType mimeType)
            throws IOException {
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
