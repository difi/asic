package no.difi.commons.asic.api;

import java.util.List;

/**
 * @author erlend
 */
public interface MessageDigestAlgorithm {

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
