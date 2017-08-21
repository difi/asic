package no.difi.commons.asic.code;

import no.difi.commons.asic.api.EncryptionAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/**
 * https://www.w3.org/TR/xmlsec-algorithms/#aes
 *
 * @author erlend
 */
public enum EncryptionAlgorithms implements EncryptionAlgorithm {

    AES256_CBC("2.16.840.1.101.3.4.1.42",
            "http://www.w3.org/2001/04/xmlenc#aes256-cbc"
    ),

    AES256_GCM("2.16.840.1.101.3.4.1.46",
            "http://www.w3.org/2009/xmlenc11#aes256-gcm"
    );

    private String oid;

    private String[] uri;

    EncryptionAlgorithms(String oid, String... uri) {
        this.oid = oid;
        this.uri = uri;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return new ASN1ObjectIdentifier(oid);
    }

    @Override
    public String getUri() {
        return uri[0];
    }

    @Override
    public boolean matchesOid(String oid) {
        return this.oid.equals(oid);
    }

    @Override
    public boolean matchesUri(String uri) {
        for (String u : this.uri)
            if (u.equals(uri))
                return true;
        return false;
    }
}
