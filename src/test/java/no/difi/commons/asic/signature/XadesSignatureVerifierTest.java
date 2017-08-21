package no.difi.commons.asic.signature;

import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.model.Container;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public class XadesSignatureVerifierTest {

    @Test(enabled = false)
    public void simple() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/xades/signatures-sdp.xml")) {
            Container container = new Container(Container.Mode.READER);
            AsicReaderLayer asicReaderLayer = Mockito.mock(AsicReaderLayer.class);
            Mockito.when(asicReaderLayer.getContent()).thenReturn(inputStream);

            XadesSignatureVerifier.INSTANCE.handle(asicReaderLayer, "signatures.xml", container, null, null);
        }
    }
}
