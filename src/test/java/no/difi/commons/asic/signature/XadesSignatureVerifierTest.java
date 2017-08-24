package no.difi.commons.asic.signature;

import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.builder.BuildHandler;
import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.FileCache;
import no.difi.commons.asic.model.MimeType;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public class XadesSignatureVerifierTest {

    @Test
    public void simple() throws IOException {
        Properties properties = Builder.raw().build();

        Container container = Mockito.mock(Container.class);
        FileCache fileCache = Mockito.mock(FileCache.class);

        try (InputStream inputStream = getClass().getResourceAsStream("/xades/signatures-sdp.xml")) {
            AsicReaderLayer asicReaderLayer = Mockito.mock(AsicReaderLayer.class);
            Mockito.when(asicReaderLayer.getContent()).thenReturn(inputStream);

            XadesSignatureVerifier.INSTANCE.handle(asicReaderLayer, "signatures.xml", container, fileCache, properties);
        }

        Mockito.verify(container).update("hoveddokument.txt", DataObject.Type.DATA, MimeType.APPLICATION_XML);
        Mockito.verify(container).update("ts_103174v020201p.pdf", DataObject.Type.DATA, MimeType.APPLICATION_XML);
        Mockito.verify(container).update("ts_102918v010301p.pdf", DataObject.Type.DATA, MimeType.APPLICATION_XML);
        Mockito.verify(container).update("ts_103171v020101p.pdf", DataObject.Type.DATA, MimeType.APPLICATION_XML);
        Mockito.verify(container).update("ts_101903v010402p.pdf", DataObject.Type.DATA, MimeType.APPLICATION_XML);
        Mockito.verify(container).update("manifest.xml", DataObject.Type.DATA, MimeType.APPLICATION_XML);
        Mockito.verifyNoMoreInteractions(container);

        Mockito.verifyZeroInteractions(fileCache);
    }
}
