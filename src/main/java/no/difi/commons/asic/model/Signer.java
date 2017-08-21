package no.difi.commons.asic.model;

import java.io.Serializable;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class Signer implements Serializable {

    private static final long serialVersionUID = -6249652683875153984L;

    private X509Certificate certificate;

    public Signer(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signer signer = (Signer) o;

        return certificate.equals(signer.certificate);
    }

    @Override
    public int hashCode() {
        return certificate.hashCode();
    }
}
