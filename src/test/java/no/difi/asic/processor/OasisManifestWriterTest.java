package no.difi.asic.processor;

import no.difi.asic.api.WriterProcessor;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.test.TestWriterLayer;
import no.difi.asic.util.MimeTypes;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erlend
 */
public class OasisManifestWriterTest {

    private WriterProcessor writerProcessor = new OasisManifestWriter();

    @Test
    public void simple() throws IOException {
        TestWriterLayer writerLayer = new TestWriterLayer();

        Container container = new Container(Container.Mode.WRITER);
        container.update("file_1.xml", DataObject.Type.DATA, MimeTypes.XML);
        container.update("file_2.xml", DataObject.Type.DATA, MimeTypes.XML);

        writerProcessor.perform(writerLayer, container, null);

        List<String> writtenFiles = new ArrayList<>(writerLayer.getFiles());

        Assert.assertEquals(writtenFiles.size(), 1);
        Assert.assertEquals(writtenFiles.get(0), "META-INF/manifest.xml");
    }
}
