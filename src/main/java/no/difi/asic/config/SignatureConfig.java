package no.difi.asic.config;

import no.difi.asic.annotation.Signature;
import no.difi.asic.api.SignatureCreator;
import no.difi.asic.api.SignatureVerifier;
import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.lang.AsicException;

/**
 * @author erlend
 */
public class SignatureConfig {

    private Signature source;

    private SignatureCreator signatureCreator;

    private SignatureVerifier signatureVerifier;

    SignatureConfig(Signature signature) throws AsicException {
        source = signature;

        try {
            signatureCreator = signature.signatureCreator().newInstance();
            signatureVerifier = signature.signatureVerifier().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AsicException("Unable to initiate signature implementation.", e);
        }
    }

    public MessageDigestAlgorithm[] getDataObjectAlgorithm() {
        return source.dataObjectAlgorithm();
    }

    public MessageDigestAlgorithm getSignatureAlgorithm() {
        return source.signatureAlgorithm();
    }

    public SignatureCreator getSignatureCreator() {
        return signatureCreator;
    }

    public SignatureVerifier getSignatureVerifier() {
        return signatureVerifier;
    }

    public Signature getSource() {
        return source;
    }
}
