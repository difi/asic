package no.difi.asic.signature;

import no.difi.asic.api.SignatureVerifier;

/**
 * @author erlend
 */
public class CadesSignatureVerifier extends CadesCommons implements SignatureVerifier {

    @Override
    public boolean isSignatureFile(String filename) {
        return false;
    }
}
