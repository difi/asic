package no.difi.commons.asic.processor;

import no.difi.commons.asic.Asic;
import no.difi.commons.asic.annotation.Processor;
import no.difi.commons.asic.api.AsicWriterLayer;
import no.difi.commons.asic.api.WriterProcessor;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.MimeType;
import no.difi.commons.asic.util.MimeTypes;
import no.difi.commons.asic.jaxb.opendocument.manifest.FileEntry;
import no.difi.commons.asic.jaxb.opendocument.manifest.Manifest;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author erlend
 */
@Processor(Processor.State.AFTER_SIGNATURE)
public class OasisManifestWriter extends OasisManifestCommons implements WriterProcessor {

    public static WriterProcessor INSTANCE = new OasisManifestWriter();

    @Override
    public void perform(AsicWriterLayer asicWriterLayer, Container container, Properties properties)
            throws IOException {
        Manifest manifest = OBJECT_FACTORY.createManifest();

        manifest.getFileEntry().add(createFileEntity("/", MimeType.forString(Asic.MIMETYPE_ASICE)));

        for (DataObject dataObject : container.getDataObjects())
            manifest.getFileEntry().add(createFileEntity(dataObject.getFilename(), dataObject.getMimeType()));

        try (OutputStream outputStream = asicWriterLayer.addContent(DataObject.Type.METADATA, FILENAME, MimeTypes.XML)) {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.marshal(manifest, outputStream);
        } catch (JAXBException e) {
            throw new AsicException("Unable to create XML for Oasis Manifest.", e);
        }
    }

    private FileEntry createFileEntity(String filename, MimeType mimeType) {
        FileEntry fileEntry = OBJECT_FACTORY.createFileEntry();
        fileEntry.setFullPath(filename);
        fileEntry.setMediaType(mimeType.toString());

        return fileEntry;
    }
}
