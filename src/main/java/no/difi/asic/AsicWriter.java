package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.annotation.Processor;
import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.MimeType;
import no.difi.asic.security.MultiMessageDigest;
import no.difi.asic.util.MimeTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author erlend
 */
public class AsicWriter implements Closeable {

    private OutputStream outputStream;

    private no.difi.asic.api.AsicWriterLayer asicWriterLayer;

    private boolean closeStreamOnClose;

    private boolean signed = false;

    protected ConfigurationWrapper configuration;

    private Container container = new Container(Container.Mode.WRITER);

    protected List<X509Certificate> certificates;

    protected List<KeyStore.PrivateKeyEntry> keyEntries;

    protected AsicWriter(OutputStream outputStream, boolean closeStreamOnClose, ConfigurationWrapper configuration)
            throws IOException, AsicException {
        this.configuration = configuration;
        this.outputStream = outputStream;
        this.closeStreamOnClose = closeStreamOnClose;

        MultiMessageDigest messageDigest =
                new MultiMessageDigest(configuration.getSignature().getDataObjectAlgorithm());
        this.asicWriterLayer = new AsicWriterLayer(outputStream, messageDigest, container);

        configuration.process(Processor.State.INITIAL, asicWriterLayer, container);
    }

    public void setRootFile(String filename) throws AsicException {
        if (!configuration.getSignature().getSignatureCreator().supportsRootFile())
            throw new AsicException("Root file is not supported with current configuration.");

        container.setRootFile(filename);
    }

    public void add(File file, String filename, MimeType mimeType) throws IOException, AsicException {
        add(file.toPath(), filename, mimeType);
    }

    public void add(Path path, String filename, MimeType mimeType) throws IOException, AsicException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            add(inputStream, filename != null ? filename : path.toFile().getName(), mimeType);
        }
    }

    public void add(InputStream inputStream, String filename, MimeType mimeType) throws IOException, AsicException {
        try (OutputStream outputStream = add(filename, mimeType)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }

    public OutputStream add(String filename, MimeType mimeType) throws IOException, AsicException {
        if (signed)
            throw new AsicException("Adding content to container after signing container is not supported.");

        if (filename.toUpperCase().startsWith("META-INF/"))
            throw new AsicException("Adding files to META-INF is not allowed.");

        return asicWriterLayer.addContent(DataObject.Type.DATA, filename,
                mimeType != null ? mimeType : MimeTypes.detect(filename));
    }

    public void sign() throws IOException, AsicException {
        configuration.process(Processor.State.BEFORE_SIGNATURE, asicWriterLayer, container);

        configuration.getSignature().getSignatureCreator()
                .create(asicWriterLayer, container, keyEntries, configuration.getSignature());

        signed = true;

        configuration.process(Processor.State.AFTER_SIGNATURE, asicWriterLayer, container);
    }

    @Override
    public void close() throws IOException {
        if (!signed)
            throw new IOException("Unsigned ASiC-E is not possible.");

        asicWriterLayer.close();

        if (closeStreamOnClose)
            outputStream.close();
    }
}
