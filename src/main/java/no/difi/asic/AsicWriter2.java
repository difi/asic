package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.config.ConfigurationWrapper;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.util.BCUtil;
import no.difi.asic.util.MimeTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;

/**
 * @author erlend
 */
public class AsicWriter2 implements Closeable {

    private OutputStream outputStream;

    private AsicOutputStream2 asicOutputStream;

    private MessageDigest messageDigest;

    private ConfigurationWrapper configuration;

    private Container container = new Container();

    public AsicWriter2(OutputStream outputStream, ConfigurationWrapper configuration)
            throws AsicExcepion {
        this.outputStream = outputStream;
        this.asicOutputStream = new AsicOutputStream2(outputStream);
        this.configuration = configuration;

        this.messageDigest = BCUtil.createMessageDigest(configuration.getSignature().getDataObjectAlgorithm());
    }

    public void setRootFile(String filename) throws AsicExcepion {
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
        final MimeType mimeType1 = mimeType != null ? mimeType : MimeTypes.detect(filename);

        asicOutputStream.nextEntry(filename);

        messageDigest.reset();
        OutputStream outputStream = new DigestOutputStream(asicOutputStream, messageDigest);

        return new FilterOutputStream(outputStream) {
            @Override
            public void close() throws IOException {
                asicOutputStream.closeEntry();

                container.add(new DataObject(filename, mimeType1.toString(), messageDigest.digest()));
            }
        };
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

    public void sign(KeyStore.PrivateKeyEntry privateKeyEntry) throws IOException, AsicExcepion {
        configuration.getSignature().getSignatureCreator()
                .create(container, privateKeyEntry, configuration.getSignature());
    }

    @Override
    public void close() throws IOException {
        asicOutputStream.close();
    }
}
