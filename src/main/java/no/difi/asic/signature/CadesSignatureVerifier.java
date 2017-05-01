package no.difi.asic.signature;

import com.google.common.io.ByteStreams;
import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.SignatureVerifier;
import no.difi.asic.code.MessageDigestAlgorithm;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.MimeType;
import no.difi.asic.util.MimeTypes;
import no.difi.commons.asic.jaxb.cades.ASiCManifestType;
import no.difi.commons.asic.jaxb.cades.DataObjectReferenceType;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author erlend
 */
public class CadesSignatureVerifier extends CadesCommons implements SignatureVerifier {

    protected static final Pattern PATTERN_MANIFEST =
            Pattern.compile("META-INF/asicmanifest(.*)\\.xml", Pattern.CASE_INSENSITIVE);

    static final Pattern PATTERN_SIGNATURE =
            Pattern.compile("META-INF/signature(.*)\\.p7s", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean supports(String filename) {
        return PATTERN_MANIFEST.matcher(filename).matches() || PATTERN_SIGNATURE.matcher(filename).matches();
    }

    @Override
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container)
            throws IOException {
        if (PATTERN_MANIFEST.matcher(filename).matches()) {
            // Parse manifest
            container.update(filename, DataObject.Type.MANIFEST, MimeTypes.XML);

            ASiCManifestType aSiCManifest;

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ByteStreams.copy(asicReaderLayer.getContent(), baos);

                String xml = baos.toString()
                        .replace("http://uri.etsi.org/02918/v1.1.1#", "http://uri.etsi.org/02918/v1.2.1#")
                        .replace("http://uri.etsi.org/2918/v1.2.1#", "http://uri.etsi.org/02918/v1.2.1#");

                Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
                aSiCManifest = unmarshaller.unmarshal(
                        new StreamSource(new ByteArrayInputStream(xml.getBytes())), ASiCManifestType.class).getValue();
            } catch (JAXBException e) {
                throw new AsicException(e.getMessage(), e);
            }

            for (DataObjectReferenceType reference : aSiCManifest.getDataObjectReference()) {
                container.update(reference.getURI(), DataObject.Type.DATA,
                        MimeType.forString(reference.getMimeType()));

                container.verify(null, reference.getURI(),
                        MessageDigestAlgorithm.findByUri(reference.getDigestMethod().getAlgorithm()),
                        reference.getDigestValue());

                if (reference.isRootfile() != null && reference.isRootfile())
                    container.setRootFile(reference.getURI());
            }
        } else {
            container.update(filename, DataObject.Type.DETACHED_SIGNATURE, MimeType.forString(SIGNATURE_MIME_TYPE));
            // Validate signature
        }
    }
}
