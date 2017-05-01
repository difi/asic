package no.difi.asic;

import com.google.common.io.ByteStreams;
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
    public void beforeClass() throws IOException {
        asicReaderFactory = AsicReaderFactory2.newFactory().build();
    }

    @Test
    public void simple() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/asic-cades-test-valid.asice");
             AsicReader2 asicReader = asicReaderFactory.open(inputStream).build()) {

            String filename;
            while ((filename = asicReader.next()) != null) {
                System.out.println(filename);

                try (InputStream content = asicReader.getContent()) {
                    ByteStreams.copy(content, ByteStreams.nullOutputStream());
                }
            }

            System.out.println(asicReader);
        }
    }
}
