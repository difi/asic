package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.lang.AsicException;
import no.difi.asic.util.KeyStoreUtil;
import no.difi.asic.util.MimeTypes;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.security.KeyStore;

/**
 * @author erlend
 */
public class AsicWriterTest {

    KeyStore.PrivateKeyEntry keyEntry;

    @BeforeClass
    public void beforeClass() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/keystore.jks")) {
            keyEntry = KeyStoreUtil.load(inputStream, "changeit", "selfsigned", "changeit");
        }
    }

    @Test
    public void simple() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory(Configuration.LEGACY)
                .signWith(keyEntry)
                .build();

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

        AsicVerifier asicVerifier = AsicVerifierFactory.newFactory()
                .verify(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        Assert.assertEquals(asicVerifier.getAsicManifest().getFile().size(), 2);
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerExceptionWhenAddingMetadataFile() throws IOException {
        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory(Configuration.LEGACY)
                .signWith(keyEntry)
                .build();

        AsicWriter asicWriter = asicWriterFactory.newContainer(ByteStreams.nullOutputStream()).build();

        try (InputStream inputStream = getClass().getResourceAsStream("/bii-envelope.xml")) {
            asicWriter.add(inputStream, "META-INF/bii-envelope.xml", MimeTypes.XML);
        }

        asicWriter.sign();
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerExceptionWhenAddingAfterSign() throws IOException {
        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory(Configuration.LEGACY)
                .signWith(keyEntry)
                .build();

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
}
