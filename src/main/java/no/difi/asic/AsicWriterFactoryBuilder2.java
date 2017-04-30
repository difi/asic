package no.difi.asic;

import no.difi.asic.api.AsicWriterBuilder;
import no.difi.asic.lang.AsicException;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder to create AsicWriterFactory. Allows defining initial certificates and keys.
 *
 * @author erlend
 */
class AsicWriterFactoryBuilder2 implements AsicWriterBuilder<AsicWriterFactory2> {

    /**
     * Enum provided to be used for configuration.
     */
    private Enum configuration;

    /**
     * Certificates used for encryption.
     */
    protected List<X509Certificate> certificates = new ArrayList<>();

    /**
     * Certificates used for signing.
     */
    protected List<KeyStore.PrivateKeyEntry> keyEntries = new ArrayList<>();

    /**
     * Protected constructor for this builder.
     */
    protected AsicWriterFactoryBuilder2(Enum configuration) {
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriterBuilder<AsicWriterFactory2> encryptFor(X509Certificate certificate) {
        certificates.add(certificate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriterBuilder<AsicWriterFactory2> signBy(KeyStore.PrivateKeyEntry privateKeyEntry) {
        keyEntries.add(privateKeyEntry);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsicWriterFactory2 build() throws AsicException {
        AsicWriterFactory2 asicWriterFactory = new AsicWriterFactory2(this, configuration);
        asicWriterFactory.certificates = certificates;
        asicWriterFactory.keyEntries = keyEntries;

        return asicWriterFactory;
    }
}
