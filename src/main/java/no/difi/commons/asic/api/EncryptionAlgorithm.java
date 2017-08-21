package no.difi.commons.asic.api;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

import java.util.List;

/**
 * @author erlend
 */
public interface EncryptionAlgorithm {

    ASN1ObjectIdentifier getOid();

    String getUri();

    boolean matchesOid(String oid);

    boolean matchesUri(String uri);

    static EncryptionAlgorithm of(String oid, String... uri) {
        return new DefaultEncryptionAlgorithm(oid, uri);
    }

    static boolean oidInList(String oid, List<EncryptionAlgorithm> algorithms) {
        for (EncryptionAlgorithm encryptionAlgorithm : algorithms)
            if (encryptionAlgorithm.matchesOid(oid))
                return true;

        return false;
    }

    static boolean uriInList(String uri, List<EncryptionAlgorithm> algorithms) {
        for (EncryptionAlgorithm encryptionAlgorithm : algorithms)
            if (encryptionAlgorithm.matchesUri(uri))
                return true;

        return false;
    }

    class DefaultEncryptionAlgorithm implements EncryptionAlgorithm {

        private String oid;

        private String[] uri;

        private DefaultEncryptionAlgorithm(String oid, String... uri) {
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
}
