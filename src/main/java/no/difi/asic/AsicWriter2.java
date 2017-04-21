package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.api.AsicWriterLayer;
import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.model.Container;
import no.difi.asic.model.MimeType;
import no.difi.asic.util.BCUtil;
import no.difi.asic.util.MimeTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author erlend
 */
public class AsicWriter2 implements Closeable {

    private OutputStream outputStream;

    private AsicOutputStream2 asicOutputStream;

    private AsicWriterLayer asicWriterLayer;

    private boolean closeStreamOnClose;

    private boolean signed = false;

    private MessageDigest messageDigest;

    protected ConfigurationWrapper configuration;

    private Container container = new Container();

    protected List<X509Certificate> certificates;

    protected List<KeyStore.PrivateKeyEntry> keyEntries;

    protected AsicWriter2(OutputStream outputStream, boolean closeStreamOnClose, ConfigurationWrapper configuration)
            throws IOException, AsicExcepion {
        this.configuration = configuration;
        this.messageDigest = BCUtil.createMessageDigest(configuration.getSignature().getDataObjectAlgorithm());

        this.outputStream = outputStream;
        this.closeStreamOnClose = closeStreamOnClose;
        this.asicOutputStream = new AsicOutputStream2(outputStream);
        this.asicWriterLayer = new AsicWriterLayer2(asicOutputStream, messageDigest, container);
    }

    public void setRootFile(String filename) throws AsicExcepion {
        if (!configuration.getSignature().getSignatureCreator().supportsRootFile())
            throw new AsicExcepion("Root file is not supported with current configuration.");

        container.setRootFile(filename);
    }

    public void add(File file, String filename, MimeType mimeType) throws IOException, AsicExcepion {
        add(file.toPath(), filename, mimeType);
    }

    public void add(Path path, String filename, MimeType mimeType) throws IOException, AsicExcepion {
        try (InputStream inputStream = Files.newInputStream(path)) {
            add(inputStream, filename != null ? filename : path.toFile().getName(), mimeType);
        }
    }

    public void add(InputStream inputStream, String filename, MimeType mimeType) throws IOException, AsicExcepion {
        try (OutputStream outputStream = add(filename, mimeType)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }

    public OutputStream add(final String filename, MimeType mimeType) throws IOException, AsicExcepion {
        if (signed)
            throw new AsicExcepion("Adding content to container after signing container is not supported.");

        return asicWriterLayer.addContent(filename, mimeType != null ? mimeType : MimeTypes.detect(filename));
    }

    public void addEncrypted(File file, String filename, MimeType mimeType) throws IOException, AsicExcepion {
        addEncrypted(file.toPath(), filename, mimeType);
    }

    public void addEncrypted(Path path, String filename, MimeType mimeType) throws IOException, AsicExcepion {
        try (InputStream inputStream = Files.newInputStream(path)) {
            addEncrypted(inputStream, filename != null ? filename : path.toFile().getName(), mimeType);
        }
    }

    public void addEncrypted(InputStream inputStream, String filename, MimeType mimeType)
            throws IOException, AsicExcepion {
        try (OutputStream outputStream = addEncrypted(filename, mimeType)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }

    public OutputStream addEncrypted(String filename, MimeType mimeType) throws IOException, AsicExcepion {
        return add(filename, mimeType);
    }

    public void sign() throws IOException, AsicExcepion {
        configuration.getSignature().getSignatureCreator()
                .create(asicWriterLayer, container, keyEntries, configuration.getSignature());

        signed = true;
    }

    @Override
    public void close() throws IOException {
        if (!signed)
            throw new IOException("Unsigned ASiC-E is not possible.");

        asicOutputStream.close();

        if (closeStreamOnClose)
            outputStream.close();
    }
}
