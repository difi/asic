package no.difi.asic;

import no.difi.asic.code.MessageDigestAlgorithm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Deprecated
public class AsicVerifierFactory {

    public static AsicVerifierFactory newFactory() {
        return newFactory(MessageDigestAlgorithm.SHA256);
    }

    static AsicVerifierFactory newFactory(MessageDigestAlgorithm messageDigestAlgorithm) {
        return new AsicVerifierFactory(messageDigestAlgorithm);
    }

    private MessageDigestAlgorithm messageDigestAlgorithm;

    private AsicVerifierFactory(MessageDigestAlgorithm messageDigestAlgorithm) {
        this.messageDigestAlgorithm = messageDigestAlgorithm;
    }

    public AsicVerifier verify(File file) throws IOException {
        return verify(file.toPath());
    }

    public AsicVerifier verify(Path file) throws IOException {
        return verify(Files.newInputStream(file));
    }

    public AsicVerifier verify(InputStream inputStream) throws IOException {
        return new AsicVerifier(messageDigestAlgorithm, inputStream);
    }
}
