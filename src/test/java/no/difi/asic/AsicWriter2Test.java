package no.difi.asic;

import no.difi.asic.util.KeyStoreUtil;
import no.difi.asic.util.MimeTypes;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author erlend
 */
public class AsicWriter2Test {

    @Test
    public void simple() throws Exception {
        AsicWriterFactory2 asicWriterFactory = AsicWriterFactory2.newFactory(Configuration.PAYMENT);

        Path path = Paths.get("target/asicwriter2-test.asice");
        try (OutputStream outputStream = Files.newOutputStream(path);
             AsicWriter2 asicWriter = asicWriterFactory.newContainer(outputStream)) {

            try (InputStream inputStream = getClass().getResourceAsStream("/bii-envelope.xml")) {
                asicWriter.add(inputStream, "bii-envelope.xml", MimeTypes.XML);
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/bii-trns081.xml")) {
                asicWriter.add(inputStream, "bii-trns081.xml", MimeTypes.XML);
            }

            asicWriter.setRootFile("bii-envelope.xml");

            try (InputStream inputStream = getClass().getResourceAsStream("/kontaktinfo-client-test.jks")) {
                asicWriter.sign(KeyStoreUtil.load(inputStream, "changeit", "client_alias", "changeit"));
            }
        }
    }
}
