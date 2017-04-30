package no.difi.asic;

import no.difi.asic.lang.AsicException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public class AsicReader2Test {

    private AsicReaderFactory2 asicReaderFactory;

    @BeforeClass
    public void beforeClass() throws IOException, AsicException {
        asicReaderFactory = AsicReaderFactory2.newFactory().build();
    }

    @Test
    public void simple() throws IOException, AsicException {
        try (InputStream inputStream = getClass().getResourceAsStream("/asic-cades-test-valid.asice");
             AsicReader2 asicReader = asicReaderFactory.open(inputStream).build()) {

            String filename;
            while ((filename = asicReader.next()) != null) {
                System.out.println(filename);
            }
        }
    }
}
