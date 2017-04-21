package no.difi.asic.code;

import no.difi.asic.annotation.OIDValue;
import no.difi.asic.annotation.URIValue;

/**
 * @author erlend
 */
public enum EncryptionAlgorithm {

    @OIDValue("2.16.840.1.101.3.4.1.42")
    @URIValue("http://www.w3.org/2001/04/xmlenc#aes256-cbc")
    AES256_CBC,

    @OIDValue("2.16.840.1.101.3.4.1.46")
    @URIValue("http://www.w3.org/2009/xmlenc11#aes256-gcm")
    AES256_GCM,

}
