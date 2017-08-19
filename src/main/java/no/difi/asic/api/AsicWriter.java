package no.difi.asic.api;

import com.google.common.io.ByteStreams;
import no.difi.asic.builder.Property;
import no.difi.asic.code.EncryptionAlgorithm;
import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.encryption.CmsEncryptionFilter;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.MimeType;
import no.difi.asic.processor.OasisManifestWriter;
import no.difi.asic.signature.CadesSignatureCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author erlend
 */
public interface AsicWriter extends Closeable {

    Property<EncryptionAlgorithm> ENCRYPTION_ALGORITHM = Property.create(EncryptionAlgorithm.AES256_GCM);

    Property<X509Certificate> ENCRYPTION_CERTIFICATES = Property.create();

    Property<EncryptionFilter> ENCRYPTION_FILTER = Property.create(CmsEncryptionFilter.INSTANCE);

    Property<List<KeyStore.PrivateKeyEntry>> SIGNATURE_CERTIFICATES = Property.create();

    Property<MessageDigestAlgorithm> SIGNATURE_OBJECT_ALGORITHM = Property.create(MessageDigestAlgorithm.SHA256);

    Property<MessageDigestAlgorithm> SIGNATURE_ALGORITHM = Property.create(MessageDigestAlgorithm.SHA256);

    Property<SignatureCreator> SIGNATURE_CREATOR = Property.create(CadesSignatureCreator.INSTANCE);

    Property<List<WriterProcessor>> PROCESSORS = Property.createList(OasisManifestWriter.INSTANCE);



    default void add(File file, MimeType mimeType) throws IOException {
        add(file.toPath(), mimeType);
    }

    default void add(File file, String filename, MimeType mimeType) throws IOException {
        add(file.toPath(), filename, mimeType);
    }

    default void add(Path path, MimeType mimeType) throws IOException {
        add(path, path.toFile().getName(), mimeType);
    }

    default void add(Path path, String filename, MimeType mimeType) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            add(inputStream, filename, mimeType);
        }
    }

    default void add(InputStream inputStream, String filename, MimeType mimeType) throws IOException {
        try (OutputStream outputStream = add(filename, mimeType)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }

    OutputStream add(String filename, MimeType mimeType) throws IOException;

    void setRootFile(String filename) throws AsicException;

    void sign() throws IOException;

}
