package no.difi.asic;

import no.difi.asic.lang.AsicException;
import no.difi.asic.util.KeyStoreUtil;
import no.difi.asic.util.MimeTypes;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.security.KeyStore;

/**
 * @author erlend
 */
public class AsicWriter2Test {

    @Test
    public void simple() throws IOException, AsicException {
        KeyStore.PrivateKeyEntry keyEntry;
        try (InputStream inputStream = getClass().getResourceAsStream("/kontaktinfo-client-test.jks")) {
            keyEntry = KeyStoreUtil.load(inputStream, "changeit", "client_alias", "changeit");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        AsicWriterFactory2 asicWriterFactory = AsicWriterFactory2.newFactory(Configuration.LAGACY)
                .signBy(keyEntry)
                .build();

        // Path path = Paths.get("target/asicwriter2-test.asice");
        // try (OutputStream outputStream = Files.newOutputStream(path);
        try (OutputStream outputStream = byteArrayOutputStream;
             AsicWriter2 asicWriter = asicWriterFactory.newContainer(outputStream).build()) {

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
}
