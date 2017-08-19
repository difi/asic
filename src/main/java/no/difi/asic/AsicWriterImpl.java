package no.difi.asic;

import no.difi.asic.annotation.Processor;
import no.difi.asic.api.AsicWriter;
import no.difi.asic.api.AsicWriterLayer;
import no.difi.asic.builder.Properties;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.MimeType;
import no.difi.asic.security.MultiMessageDigest;
import no.difi.asic.util.MimeTypes;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * @author erlend
 */
class AsicWriterImpl implements AsicWriter {

    private OutputStream outputStream;

    private AsicWriterLayer asicWriterLayer;

    private boolean closeStreamOnClose;

    private boolean signed = false;

    private Container container = new Container(Container.Mode.WRITER);

    private Properties properties;

    protected AsicWriterImpl(Properties properties, OutputStream outputStream, boolean closeStreamOnClose)
            throws IOException {
        this.properties = properties;
        this.outputStream = outputStream;
        this.closeStreamOnClose = closeStreamOnClose;

        MultiMessageDigest messageDigest =
                new MultiMessageDigest(properties.get(AsicWriter.SIGNATURE_OBJECT_ALGORITHM));
        this.asicWriterLayer = new AsicWriterLayerImpl(outputStream, messageDigest, container);

        performProcessors(Processor.State.INITIAL);
    }

    @Override
    public OutputStream add(String filename, MimeType mimeType) throws IOException {
        if (signed)
            throw new AsicException("Adding content to container after signing container is not supported.");

        if (filename.toUpperCase().startsWith("META-INF/"))
            throw new AsicException("Adding files to META-INF is not allowed.");

        return asicWriterLayer.addContent(DataObject.Type.DATA, filename,
                mimeType != null ? mimeType : MimeTypes.detect(filename));
    }

    @Override
    public void setRootFile(String filename) throws AsicException {
        if (!properties.get(AsicWriter.SIGNATURE_CREATOR).supportsRootFile())
            throw new AsicException("Root file is not supported with current configuration.");

        container.setRootFile(filename);
    }

    @Override
    public void sign() throws IOException {
        performProcessors(Processor.State.BEFORE_SIGNATURE);

        properties.get(AsicWriter.SIGNATURE_CREATOR)
                .create(asicWriterLayer, container,
                        properties.get(AsicWriter.SIGNATURE_CERTIFICATES),
                        properties.get(AsicWriter.SIGNATURE_OBJECT_ALGORITHM),
                        properties.get(AsicWriter.SIGNATURE_ALGORITHM));

        signed = true;

        performProcessors(Processor.State.AFTER_SIGNATURE);
    }

    @Override
    public void close() throws IOException {
        if (!signed)
            throw new IOException("Unsigned ASiC-E is not possible.");

        asicWriterLayer.close();

        if (closeStreamOnClose)
            outputStream.close();
    }

    private void performProcessors(Processor.State state) throws IOException {
        try {
            properties.get(AsicWriter.PROCESSORS).stream()
                    .filter(p -> p.getState().equals(state))
                    .forEach(p -> {
                        try {
                            p.perform(asicWriterLayer, container);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }
}
