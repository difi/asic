package no.difi.asic.code;

/**
 * ETSI TS 102 918 v1.1.1 and v1.3.1 differs in Annex B part B.3 when it comes to namespace used to declare identifiers
 * for hashing algorithms. Version 1.1.1 uses XMLDSig namespace and version 1.3.1 uses XMLEnc namespace. As it is most
 * correct to use XMLDSig namespace identifiers in XMLDSig elements, this is the preferred namespace in this
 * implementation. XMLEnc namespace is added to support parsing of manifests using this namespace.
 * <p>
 * https://www.w3.org/TR/xmlsec-algorithms/#sha
 *
 * @author erlend
 */
public enum MessageDigestAlgorithm {

    // MD5 is omitted.

    SHA1("SHA1",
            "http://www.w3.org/2000/09/xmldsig#sha1",
            "http://www.w3.org/2001/04/xmlenc#sha1"),

    SHA224("SHA224",
            "http://www.w3.org/2001/04/xmldsig-more#sha224"),

    SHA256("SHA256",
            "http://www.w3.org/2000/09/xmldsig#sha256",
            "http://www.w3.org/2001/04/xmlenc#sha256"),

    SHA384("SHA384",
            "http://www.w3.org/2001/04/xmldsig-more#sha384"),

    SHA512("SHA512",
            "http://www.w3.org/2001/04/xmlenc#sha512",
            "http://www.w3.org/2000/09/xmldsig#sha512"),

    RIPEMD160("RIPEMD160",
            "http://www.w3.org/2001/04/xmlenc#ripemd160");

    private final String str;

    private final String[] uri;

    MessageDigestAlgorithm(String str, String... uri) {
        this.str = str;
        this.uri = uri;
    }

    public String getString() {
        return str;
    }

    public String getURI() {
        return uri[0];
    }

    public static MessageDigestAlgorithm findByUri(String uri) {
        for (MessageDigestAlgorithm algorithm : values())
            for (String u : algorithm.uri)
                if (u.equals(uri))
                    return algorithm;

        throw new IllegalArgumentException(String.format("Unknown algorithm '%s'.", uri));
    }
}
