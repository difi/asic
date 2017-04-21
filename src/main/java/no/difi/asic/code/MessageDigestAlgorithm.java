package no.difi.asic.code;

import no.difi.asic.annotation.StringValue;
import no.difi.asic.annotation.URIValue;

public enum MessageDigestAlgorithm {

    @StringValue("SHA-1")
    SHA1,

    @StringValue("SHA-256")
    @URIValue("http://www.w3.org/2001/04/xmlenc#sha256")
    SHA256,

    @StringValue("SHA-384")
    @URIValue("http://www.w3.org/2001/04/xmlenc#sha384")
    SHA384,

    @StringValue("SHA-512")
    @URIValue("http://www.w3.org/2001/04/xmlenc#sha512")
    SHA512,

}
