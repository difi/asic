package no.difi.commons.asic.encryption;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.Asic;
import no.difi.commons.asic.api.DecryptionFilter;
import no.difi.commons.asic.api.EncryptionFilter;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.util.KeyStoreUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * @author erlend
 */
public class CmsAuthDataFilterTest {

    private EncryptionFilter encryptionFilter = CmsEncryptionAuthDataFilter.INSTANCE;

    private DecryptionFilter decryptionFilter = CmsDecryptionDetectorFilter.INSTANCE;

    private KeyStore.PrivateKeyEntry keyEntry;

    @BeforeClass
    public void beforeClass() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/keystore.jks")) {
            keyEntry = KeyStoreUtil.load(inputStream, "changeit", "selfsigned", "changeit");
        }
    }

    @SuppressWarnings("Duplicates")
    @Test(enabled = false)
    public void simple() throws IOException {
        Properties properties = Builder.raw()
                .set(Asic.ENCRYPTION_CERTIFICATES, (X509Certificate) keyEntry.getCertificate(), (X509Certificate) keyEntry.getCertificate())
                .set(Asic.DECRYPTION_CERTIFICATES, keyEntry)
                .build();

        byte[] content = ByteStreams.toByteArray(getClass().getResourceAsStream("/image.bmp"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (OutputStream outputStream = encryptionFilter.createFilter(byteArrayOutputStream, properties);
             InputStream inputStream = new ByteArrayInputStream(content)) {
            ByteStreams.copy(inputStream, outputStream);
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (InputStream inputStream = decryptionFilter.createFilter(byteArrayInputStream, properties)) {
            ByteStreams.copy(inputStream, result);
        }

        Assert.assertEquals(result.size(), content.length);
        Assert.assertTrue(Arrays.equals(content, result.toByteArray()));
    }
}
