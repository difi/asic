package no.difi.asic.code;

public enum MessageDigestAlgorithm {

    SHA1("SHA-1", null),

    SHA256("SHA-256", "http://www.w3.org/2001/04/xmlenc#sha256"),

    SHA384("SHA-384", "http://www.w3.org/2001/04/xmlenc#sha384"),

    SHA512("SHA-512", "http://www.w3.org/2001/04/xmlenc#sha512");

    private final String str;

    private final String uri;

    MessageDigestAlgorithm(String str, String uri) {
        this.str = str;
        this.uri = uri;
    }

    public String getString() {
        return str;
    }

    public String getURI() {
        return uri;
    }
}
