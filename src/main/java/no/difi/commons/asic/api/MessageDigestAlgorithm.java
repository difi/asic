package no.difi.commons.asic.api;

import java.util.List;

/**
 * @author erlend
 */
public interface MessageDigestAlgorithm {

    MessageDigestAlgorithm SHA1 = of("SHA1",
            "http://www.w3.org/2000/09/xmldsig#sha1",
            "http://www.w3.org/2001/04/xmlenc#sha1");

    MessageDigestAlgorithm SHA224 = of("SHA224",
            "http://www.w3.org/2001/04/xmldsig-more#sha224");

    MessageDigestAlgorithm SHA256 = of("SHA256",
            "http://www.w3.org/2001/04/xmlenc#sha256",
            "http://www.w3.org/2000/09/xmldsig#sha256");

    MessageDigestAlgorithm SHA384 = of("SHA384",
            "http://www.w3.org/2001/04/xmldsig-more#sha384");

    MessageDigestAlgorithm SHA512 = of("SHA512",
            "http://www.w3.org/2001/04/xmlenc#sha512",
            "http://www.w3.org/2000/09/xmldsig#sha512");

    MessageDigestAlgorithm RIPEMD160 = of("RIPEMD160",
            "http://www.w3.org/2001/04/xmlenc#ripemd160");

    String getString();

    String getURI();

    boolean containsUri(String uri);

    static MessageDigestAlgorithm of(String str, String... uri) {
        return new DefaultMessageDigestAlgorithm(str, uri);
    }

    static MessageDigestAlgorithm findByUri(String uri, List<MessageDigestAlgorithm> algorithms) {
        for (MessageDigestAlgorithm algorithm : algorithms)
            if (algorithm.containsUri(uri))
                return algorithm;

        return null;
    }

    class DefaultMessageDigestAlgorithm implements MessageDigestAlgorithm {

        private final String str;

        private final String[] uri;

        private DefaultMessageDigestAlgorithm(String str, String... uri) {
            this.str = str;
            this.uri = uri;
        }

        @Override
        public String getString() {
            return str;
        }

        @Override
        public String getURI() {
            return uri[0];
        }

        @Override
        public boolean containsUri(String uri) {
            for (String u : this.uri)
                if (u.equals(uri))
                    return true;

            return false;
        }
    }
}
