package no.difi.commons.asic;

import no.difi.commons.asic.api.AsicReaderFactory;
import no.difi.commons.asic.api.AsicWriterFactory;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.code.MessageDigestAlgorithms;
import no.difi.commons.asic.encryption.CmsEncryptionEnvelopedDataFilter;

/**
 * @author erlend
 */
@Deprecated
public interface LegacyAsic {

    /**
     * This builder initiates a factory initiated with properties according to the 0.9.x version of this library.
     *
     * @return Builder with legacy defaults.
     */
    static Builder<AsicReaderFactory, RuntimeException> readerFactoryBuilder() {
        return Asic.readerFactoryBuilder()
                .set(Asic.SIGNATURE_ALGORITHM,
                        MessageDigestAlgorithms.SHA1,
                        MessageDigestAlgorithms.SHA256);
    }

    /**
     * This builder initiates a factory initiated with properties according to the 0.9.x version of this library.
     *
     * @return Builder with legacy defaults.
     */
    static Builder<AsicWriterFactory, RuntimeException> writerFactoryBuilder() {
        return Asic.writerFactoryBuilder()
                .set(Asic.ENCRYPTION_FILTER,
                        CmsEncryptionEnvelopedDataFilter.INSTANCE)
                .set(Asic.SIGNATURE_ALGORITHM,
                        MessageDigestAlgorithms.SHA1);
    }
}
