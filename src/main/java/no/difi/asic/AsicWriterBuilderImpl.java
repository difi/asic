package no.difi.asic;

import no.difi.asic.api.AsicWriterBuilder;
import no.difi.asic.lang.AsicException;

import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder to create AsicWriterOld. Allows overriding certificates and keys provided when creating AsicWriterFactory.
 *
 * @author erlend
 */
class AsicWriterBuilderImpl implements AsicWriterBuilder<AsicWriter> {

    /**
     * Factory used to initiate this builder.
     */
    protected AsicWriterFactory asicWriterFactory;

    /**
     * OutputStream provided for content.
     */
    protected OutputStream outputStream;

    /**
     * Indicator to close OutputStream on AsicWriterOld#close. Determined by the factory based upon methods in
     * factory used to create AsicWriterOld.
     */
    protected boolean closeStreamOnClose;

    /**
     * Certificates used for encryption.
     */
    private List<X509Certificate> certificates = new ArrayList<>();

    /**
     * Certificates used for signing.
     */
    private List<KeyStore.PrivateKeyEntry> keyEntries = new ArrayList<>();

    /**
     * Protected constructor for this builder.
     */
    protected AsicWriterBuilderImpl() {
        // No action
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public no.difi.asic.api.AsicWriterBuilder<AsicWriter> encryptFor(X509Certificate certificate) {
        certificates.add(certificate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public no.difi.asic.api.AsicWriterBuilder<AsicWriter> signBy(KeyStore.PrivateKeyEntry privateKeyEntry) {
        keyEntries.add(privateKeyEntry);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriter build() throws IOException, AsicException {
        AsicWriter asicWriter = new AsicWriter(outputStream, closeStreamOnClose, asicWriterFactory.configuration);
        asicWriter.certificates = certificates.isEmpty() ? asicWriterFactory.certificates : certificates;
        asicWriter.keyEntries = keyEntries.isEmpty() ? asicWriterFactory.keyEntries : keyEntries;

        if (asicWriter.keyEntries.isEmpty())
            throw new AsicException("No certificates found for signing.");

        return asicWriter;
    }
}
