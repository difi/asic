package no.difi.commons.asic;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.api.AsicWriter;
import no.difi.commons.asic.api.AsicWriterFactory;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.MimeType;
import no.difi.commons.asic.util.KeyStoreUtil;
import no.difi.commons.asic.util.MimeTypes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class AsicWriterTest {

    private AsicWriterFactory asicWriterFactory;

    private KeyStore.PrivateKeyEntry keyEntry;

    @BeforeClass
    public void beforeClass() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/keystore.jks")) {
            keyEntry = KeyStoreUtil.load(inputStream, "changeit", "selfsigned", "changeit");
        }

        asicWriterFactory = Asic.legacyWriterFactoryBuilder()
                .set(Asic.SIGNATURE_CERTIFICATES, keyEntry)
                .build();
    }

    @Test
    public void simple() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Path path = Paths.get("target/asicwriter2-test.asice");
        // try (OutputStream outputStream = Files.newOutputStream(path);
        try (OutputStream outputStream = byteArrayOutputStream;
             AsicWriter asicWriter = asicWriterFactory.newContainer(outputStream).build()) {

            try (InputStream inputStream = getClass().getResourceAsStream("/bii-envelope.xml")) {
                asicWriter.add(inputStream, "bii-envelope.xml", MimeTypes.XML);
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/bii-trns081.xml")) {
                asicWriter.add(inputStream, "bii-trns081.xml", MimeTypes.XML);
            }

            asicWriter.setRootFile("bii-envelope.xml");

            asicWriter.sign();
        }

        Asic.legacyReaderFactoryBuilder().build()
                .verifyContainer(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        // Assert.assertEquals(asicVerifier.getAsicManifest().getFile().size(), 2);
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerExceptionWhenAddingMetadataFile() throws IOException {
        AsicWriter asicWriter = asicWriterFactory.newContainer(ByteStreams.nullOutputStream()).build();

        try (InputStream inputStream = getClass().getResourceAsStream("/bii-envelope.xml")) {
            asicWriter.add(inputStream, "META-INF/bii-envelope.xml", MimeTypes.XML);
        }

        asicWriter.sign();
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerExceptionWhenAddingAfterSign() throws IOException {
        AsicWriter asicWriter = asicWriterFactory.newContainer(ByteStreams.nullOutputStream()).build();

        try (InputStream inputStream = getClass().getResourceAsStream("/bii-envelope.xml")) {
            asicWriter.add(inputStream, "bii-envelope.xml", null);
        }

        asicWriter.sign();

        // This is expected to trigger exception.
        try (InputStream inputStream = getClass().getResourceAsStream("/bii-envelope.xml")) {
            asicWriter.add(inputStream, "bii-envelope.xml", MimeTypes.XML);
        }
    }

    @Test(enabled = false)
    public void withEncryption() throws IOException {
        try (AsicWriter asicWriter = asicWriterFactory.newContainer(Paths.get("encrypted.asice"))
                .set(Asic.ENCRYPTION_CERTIFICATES, (X509Certificate) keyEntry.getCertificate())
                .build()) {

            try (InputStream inputStream = getClass().getResourceAsStream("/image.bmp");
                 OutputStream outputStream = asicWriter.add("image1.bmp", MimeType.forString("image/bmp"))) {
                ByteStreams.copy(inputStream, outputStream);
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/image.bmp");
                 OutputStream outputStream = asicWriter.encryptNext().add("image2.bmp", MimeType.forString("image/bmp"))) {
                ByteStreams.copy(inputStream, outputStream);
            }

            asicWriter.sign();
        }
    }
}
