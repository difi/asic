package no.difi.commons.asic.api;

/**
 * @author erlend
 */
public interface MessageDigestAlgorithm {

    String getString();

    String getURI();

    static MessageDigestAlgorithm of(String str, String... uri) {
        return new DefaultMessageDigestAlgorithm(str, uri);
    }

    class DefaultMessageDigestAlgorithm implements MessageDigestAlgorithm {

        private final String str;

        private final String[] uri;

        private DefaultMessageDigestAlgorithm(String str, String... uri) {
            this.str = str;
            this.uri = uri;
        }

        public String getString() {
            return str;
        }

        public String getURI() {
            return uri[0];
        }

    }
}
