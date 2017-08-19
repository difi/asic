package no.difi.asic;

import no.difi.asic.api.AsicConfig;
import no.difi.asic.api.AsicReaderFactory;
import no.difi.asic.api.AsicWriterFactory;
import no.difi.asic.builder.Builder;
import no.difi.asic.code.MessageDigestAlgorithm;

public interface Asic extends AsicConfig {

    /**
     * The MIME type, which should be the very first entry in the container
     */
    String MIMETYPE_ASICE = "application/vnd.etsi.asic-e+zip";

    static Builder<AsicReaderFactory> readerFactoryBuilder() {
        return new Builder<>(AsicReaderFactoryImpl::new);
    }

    @Deprecated
    static Builder<AsicReaderFactory> legacyReaderFactoryBuilder() {
        return readerFactoryBuilder()
                .set(Asic.SIGNATURE_ALGORITHM, MessageDigestAlgorithm.SHA1);
    }

    static Builder<AsicWriterFactory> writerFactoryBuilder() {
        return new Builder<>(AsicWriterFactoryImpl::new);
    }

    @Deprecated
    static Builder<AsicWriterFactory> legacyWriterFactoryBuilder() {
        return writerFactoryBuilder()
                .set(Asic.SIGNATURE_ALGORITHM, MessageDigestAlgorithm.SHA1);
    }
}