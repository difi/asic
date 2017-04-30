package no.difi.asic;

import no.difi.asic.model.MimeType;
import no.difi.asic.processor.OasisManifestCommons;
import no.difi.commons.asic.jaxb.opendocument.manifest.FileEntry;
import no.difi.commons.asic.jaxb.opendocument.manifest.Manifest;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

@Deprecated
class OasisManifest extends OasisManifestCommons {

    public static Manifest read(InputStream inputStream) {
        return new OasisManifest(inputStream).getManifest();
    }

    private Manifest manifest = new Manifest();

    public OasisManifest(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            manifest = unmarshaller.unmarshal(new StreamSource(inputStream), Manifest.class).getValue();
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to read XML as OASIS OpenDocument Manifest.", e);
        }
    }

    public void add(String path, MimeType mimeType) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setMediaType(mimeType.toString());
        fileEntry.setFullPath(path);
        manifest.getFileEntry().add(fileEntry);
    }

    public Manifest getManifest() {
        return manifest;
    }
}
