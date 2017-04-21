package no.difi.asic;

import no.difi.asic.api.AsicWriterBuilder;
import no.difi.asic.lang.AsicExcepion;

import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder to create AsicWriter. Allows overriding certificates and keys provided when creating AsicWriterFactory.
 *
 * @author erlend
 */
class AsicWriterBuilder2 implements AsicWriterBuilder<AsicWriter2> {

    /**
     * Factory used to initiate this builder.
     */
    protected AsicWriterFactory2 asicWriterFactory2;

    /**
     * OutputStream provided for content.
     */
    protected OutputStream outputStream;

    /**
     * Indicator to close OutputStream on AsicWriter#close. Determined by the factory based upon methods in
     * factory used to create AsicWriter.
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
    protected AsicWriterBuilder2() {
        // No action
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriterBuilder<AsicWriter2> encryptFor(X509Certificate certificate) {
        certificates.add(certificate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriterBuilder<AsicWriter2> signBy(KeyStore.PrivateKeyEntry privateKeyEntry) {
        keyEntries.add(privateKeyEntry);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriter2 build() throws IOException, AsicExcepion {
        AsicWriter2 asicWriter = new AsicWriter2(outputStream, closeStreamOnClose, asicWriterFactory2.configuration);
        asicWriter.certificates = certificates.isEmpty() ? asicWriterFactory2.certificates : certificates;
        asicWriter.keyEntries = keyEntries.isEmpty() ? asicWriterFactory2.keyEntries : keyEntries;

        if (asicWriter.keyEntries.isEmpty())
            throw new AsicExcepion("No certificates found for signing.");

        return asicWriter;
    }
}
