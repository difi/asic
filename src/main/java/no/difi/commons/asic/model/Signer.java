package no.difi.commons.asic.model;

import java.io.Serializable;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class Signer implements Serializable {

    private static final long serialVersionUID = -4878897611359290498L;

    private final X509Certificate certificate;

    public Signer(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }
}
