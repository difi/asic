package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Property;
import no.difi.commons.asic.encryption.CmsDecryptionDetectorFilter;
import no.difi.commons.asic.encryption.CmsEncryptionDetectorFilter;
import no.difi.commons.asic.processor.OasisManifestReader;
import no.difi.commons.asic.processor.OasisManifestWriter;
import no.difi.commons.asic.signature.CadesSignatureCreator;
import no.difi.commons.asic.signature.CadesSignatureVerifier;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author erlend
 */
public interface AsicConfig {

    // DECRYPTION

    Property<List<KeyStore.PrivateKeyEntry>> DECRYPTION_CERTIFICATES =
            Property.create();

    Property<List<EncryptionAlgorithm>> DECRYPTION_ALGORITHM =
            Property.createList(EncryptionAlgorithm.AES256_GCM, EncryptionAlgorithm.AES256_CBC);

    Property<List<DecryptionFilter>> DECRYPTION_FILTER =
            Property.createList(CmsDecryptionDetectorFilter.INSTANCE);

    Property<Boolean> DECRYPTION_VERIFY_ALGORITHM =
            Property.create(true);

    // ENCRYPTION

    Property<EncryptionAlgorithm> ENCRYPTION_ALGORITHM =
            Property.create(EncryptionAlgorithm.AES256_GCM);

    Property<List<X509Certificate>> ENCRYPTION_CERTIFICATES =
            Property.create();

    Property<EncryptionFilter> ENCRYPTION_FILTER =
            Property.create(CmsEncryptionDetectorFilter.INSTANCE);

    // SIGNATURE

    Property<List<MessageDigestAlgorithm>> SIGNATURE_OBJECT_ALGORITHM =
            Property.createList(MessageDigestAlgorithm.SHA256);

    Property<List<MessageDigestAlgorithm>> SIGNATURE_ALGORITHM =
            Property.createList(MessageDigestAlgorithm.SHA256);

    Property<SignatureCreator> SIGNATURE_CREATOR =
            Property.create(CadesSignatureCreator.INSTANCE);

    Property<SignatureVerifier> SIGNATURE_VERIFIER =
            Property.create(CadesSignatureVerifier.INSTANCE);

    Property<List<KeyStore.PrivateKeyEntry>> SIGNATURE_CERTIFICATES =
            Property.create();

    // PROCESSORS

    Property<List<ReaderProcessor>> READER_PROCESSORS =
            Property.createList(OasisManifestReader.INSTANCE);

    Property<List<WriterProcessor>> WRITER_PROCESSORS =
            Property.createList(OasisManifestWriter.INSTANCE);

}
