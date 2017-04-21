package no.difi.asic.util;

import no.difi.asic.lang.AsicExcepion;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class KeyStoreUtil {

    public static KeyStore.PrivateKeyEntry load(InputStream keyStoreStream, String keyStorePassword,
                                                String keyAlias, String keyPassword) throws AsicExcepion {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStoreStream, keyStorePassword.toCharArray());

            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(keyAlias);
            PrivateKey key = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());

            return new KeyStore.PrivateKeyEntry(key, new Certificate[]{certificate});
        } catch (Exception e) {
            throw new AsicExcepion(String.format("Error '%s' while loading private key.", e.getMessage()), e);
        }
    }
}
