package no.difi.commons.asic;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.api.AsicReader;
import no.difi.commons.asic.api.AsicReaderFactory;
import no.difi.commons.asic.lang.AsicException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public class AsicReaderTest {

    private AsicReaderFactory asicReaderFactory;

    @BeforeClass
    public void beforeClass() throws IOException {
        asicReaderFactory = LegacyAsic.readerFactoryBuilder().build();
    }

    @Test
    public void simple() throws IOException {
        helper("/asic-cades-test-valid.asice");
    }

    @Test(expectedExceptions = AsicException.class)
    public void simpleInvalidSignature() throws IOException {
        helper("/asic-cades-test-invalid-signature.asice");
    }

    @Test(expectedExceptions = AsicException.class)
    public void simpleInvalidSigReference() throws IOException {
        helper("/asic-cades-test-invalid-sigreference.asice");
    }

    @Test(expectedExceptions = AsicException.class)
    public void simpleInvalidManifest() throws IOException {
        helper("/asic-cades-test-invalid-manifest.asice");
    }

    @Test(expectedExceptions = AsicException.class, enabled = false)
    public void simpleInvalidMetadataFile() throws IOException {
        helper("/asic-cades-test-invalid-metadata-file.asice");
    }

    @Test
    public void simpleVerifyValid() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/asic-cades-test-valid.asice")) {
            asicReaderFactory.verifyContainer(inputStream);
        }
    }

    @Test(expectedExceptions = AsicException.class)
    public void simpleVerifyInvalidSigReference() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/asic-cades-test-invalid-sigreference.asice")) {
            asicReaderFactory.verifyContainer(inputStream);
        }
    }

    private void helper(String resource) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resource);
             AsicReader asicReader = asicReaderFactory.openContainer(inputStream).build()) {

            String filename;
            while ((filename = asicReader.next()) != null) {
                Assert.assertNotNull(filename);
                try (InputStream content = asicReader.getContent()) {
                    ByteStreams.copy(content, ByteStreams.nullOutputStream());
                }
            }
        }
    }
}
