package no.difi.commons.asic;

import no.difi.commons.asic.api.AsicConfig;
import no.difi.commons.asic.api.AsicReaderFactory;
import no.difi.commons.asic.api.AsicWriterFactory;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.code.MessageDigestAlgorithms;
import no.difi.commons.asic.encryption.CmsEncryptionEnvelopedDataFilter;

public interface Asic extends AsicConfig {

    /**
     * The MIME type, which should be the very first entry in the container
     */
    String MIMETYPE_ASICE = "application/vnd.etsi.asic-e+zip";

    static Builder<AsicReaderFactory> readerFactoryBuilder() {
        return new Builder<>(AsicReaderFactoryImpl::new);
    }

    /**
     * This builder initiates a factory initiated with properties according to the 0.9.x version of this library.
     *
     * @return Builder with legacy default.
     */
    @Deprecated
    static Builder<AsicReaderFactory> legacyReaderFactoryBuilder() {
        return readerFactoryBuilder()
                .set(Asic.SIGNATURE_ALGORITHM,
                        MessageDigestAlgorithms.SHA1,
                        MessageDigestAlgorithms.SHA256);
    }

    static Builder<AsicWriterFactory> writerFactoryBuilder() {
        return new Builder<>(AsicWriterFactoryImpl::new);
    }

    /**
     * This builder initiates a factory initiated with properties according to the 0.9.x version of this library.
     *
     * @return Builder with legacy default.
     */
    @Deprecated
    static Builder<AsicWriterFactory> legacyWriterFactoryBuilder() {
        return writerFactoryBuilder()
                .set(Asic.ENCRYPTION_FILTER,
                        CmsEncryptionEnvelopedDataFilter.INSTANCE)
                .set(Asic.SIGNATURE_ALGORITHM,
                        MessageDigestAlgorithms.SHA1);
    }
}