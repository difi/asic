package no.difi.asic.code;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/**
 * https://www.w3.org/TR/xmlsec-algorithms/#aes
 *
 * @author erlend
 */
public enum EncryptionAlgorithm {

    AES256_CBC("2.16.840.1.101.3.4.1.42", new String[]{
            "http://www.w3.org/2001/04/xmlenc#aes256-cbc"
    }),

    AES256_GCM("2.16.840.1.101.3.4.1.46", new String[]{
            "http://www.w3.org/2009/xmlenc11#aes256-gcm"
    });

    private String oid;

    private String[] uri;

    EncryptionAlgorithm(String oid, String[] uri) {
        this.oid = oid;
        this.uri = uri;
    }

    public ASN1ObjectIdentifier getOid() {
        return new ASN1ObjectIdentifier(oid);
    }

    public String getUri() {
        return uri[0];
    }
}
