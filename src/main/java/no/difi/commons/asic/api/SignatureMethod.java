package no.difi.commons.asic.api;

import java.util.List;

/**
 * @author erlend
 */
public interface SignatureMethod {

    SignatureMethod SHA1_RSA = of("SHA1withRSA",
            "http://www.w3.org/2000/09/xmldsig#rsa-sha1");

    SignatureMethod SHA256_RSA = of("SHA256withRSA",
            "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");

    SignatureMethod SHA512_RSA = of("SHA512withRSA",
            "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512");

    String getString();

    String getURI();

    boolean containsUri(String uri);

    static SignatureMethod of(String str, String... uri) {
        return new DefaultSignatureMethod(str, uri);
    }

    static SignatureMethod findByUri(String uri, List<SignatureMethod> algorithms) {
        for (SignatureMethod algorithm : algorithms)
            if (algorithm.containsUri(uri))
                return algorithm;

        return null;
    }


    class DefaultSignatureMethod implements SignatureMethod {

        private String string;

        private String[] uri;

        public DefaultSignatureMethod(String string, String... uri) {
            this.string = string;
            this.uri = uri;
        }

        @Override
        public String getString() {
            return string;
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
