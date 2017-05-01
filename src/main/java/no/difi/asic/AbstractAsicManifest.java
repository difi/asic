package no.difi.asic;

import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.model.MimeType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Deprecated
abstract class AbstractAsicManifest {

    protected MessageDigestAlgorithm messageDigestAlgorithm;
    protected MessageDigest messageDigest;

    public AbstractAsicManifest(MessageDigestAlgorithm messageDigestAlgorithm) {
        this.messageDigestAlgorithm = messageDigestAlgorithm;

        // Create message digest
        try {
            messageDigest = MessageDigest.getInstance(messageDigestAlgorithm.getString());
            messageDigest.reset();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(String.format("Algorithm %s not supported", messageDigestAlgorithm.getString()), e);
        }
    }

    /**
     * @inheritDoc
     */
    public abstract void add(String filename, MimeType mimeType);
}
