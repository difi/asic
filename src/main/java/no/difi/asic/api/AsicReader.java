package no.difi.asic.api;

import com.google.common.io.ByteStreams;
import no.difi.asic.builder.Property;
import no.difi.asic.code.EncryptionAlgorithm;
import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.processor.OasisManifestReader;
import no.difi.asic.signature.CadesSignatureVerifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.List;

/**
 * @author erlend
 */
public interface AsicReader extends Closeable {

    Property<List<KeyStore.PrivateKeyEntry>> DECRYPTION_CERTIFICATES = Property.create();

    Property<EncryptionAlgorithm> DECRYPTION_ALGORITHM =
            Property.create(EncryptionAlgorithm.AES256_GCM);

    Property<List<MessageDigestAlgorithm>> SIGNATURE_OBJECT_ALGORITHM =
            Property.createList(MessageDigestAlgorithm.SHA256);

    Property<MessageDigestAlgorithm> SIGNATURE_ALGORITHM =
            Property.create(MessageDigestAlgorithm.SHA256);

    Property<SignatureVerifier> SIGNATURE_VERIFIER =
            Property.create(CadesSignatureVerifier.INSTANCE);

    Property<List<ReaderProcessor>> PROCESSORS = Property.createList(OasisManifestReader.INSTANCE);



    String next() throws IOException;

    InputStream getContent() throws IOException;

    default void writeTo(File file) throws IOException {
        writeTo(file.toPath());
    }

    default void writeTo(Path path) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            writeTo(outputStream);
        }
    }

    default void writeTo(OutputStream outputStream) throws IOException {
        try (InputStream inputStream = getContent()) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }
}
