package no.difi.asic;

import no.difi.asic.annotation.Encryption;
import no.difi.asic.annotation.Signature;
import no.difi.asic.code.EncryptionAlgorithm;
import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.encryption.CmsDecryptionFilter;
import no.difi.asic.encryption.CmsEncryptionFilter;
import no.difi.asic.signature.CadesSignatureCreator;
import no.difi.asic.signature.CadesSignatureVerifier;

/**
 * @author erlend
 */
public enum Configuration {

    @Encryption(
            algorithm = EncryptionAlgorithm.AES256_GCM,
            decryptionFilter = CmsDecryptionFilter.class,
            encryptionFilter = CmsEncryptionFilter.class)
    @Signature(
            dataObjectAlgorithm = MessageDigestAlgorithm.SHA256,
            signatureAlgorithm = MessageDigestAlgorithm.SHA1,
            signatureCreator = CadesSignatureCreator.class,
            signatureVerifier = CadesSignatureVerifier.class)
    LAGACY,

    @Encryption(
            algorithm = EncryptionAlgorithm.AES256_GCM,
            decryptionFilter = CmsDecryptionFilter.class,
            encryptionFilter = CmsEncryptionFilter.class)
    @Signature(
            dataObjectAlgorithm = MessageDigestAlgorithm.SHA256,
            signatureAlgorithm = MessageDigestAlgorithm.SHA256,
            signatureCreator = CadesSignatureCreator.class,
            signatureVerifier = CadesSignatureVerifier.class)
    PAYMENT

}
