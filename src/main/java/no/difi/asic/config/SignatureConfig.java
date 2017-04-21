package no.difi.asic.config;

import no.difi.asic.annotation.Signature;
import no.difi.asic.api.SignatureCreator;
import no.difi.asic.api.SignatureVerifier;
import no.difi.asic.lang.AsicExcepion;

/**
 * @author erlend
 */
public class SignatureConfig {

    private ValueWrapper dataObjectAlgorithm;

    private ValueWrapper signatureAlgorithm;

    private SignatureCreator signatureCreator;

    private SignatureVerifier signatureVerifier;

    SignatureConfig(Signature signature) throws AsicExcepion {
        this.dataObjectAlgorithm = new ValueWrapper(signature.dataObjectAlgorithm());
        this.signatureAlgorithm = new ValueWrapper(signature.signatureAlgorithm());

        try {
            signatureCreator = signature.signatureCreator().newInstance();
            signatureVerifier = signature.signatureVerifier().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AsicExcepion("Unable to initiate signature implementation.", e);
        }
    }

    public ValueWrapper getDataObjectAlgorithm() {
        return dataObjectAlgorithm;
    }

    public ValueWrapper getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public SignatureCreator getSignatureCreator() {
        return signatureCreator;
    }

    public SignatureVerifier getSignatureVerifier() {
        return signatureVerifier;
    }
}
