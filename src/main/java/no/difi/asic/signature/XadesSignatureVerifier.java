package no.difi.asic.signature;

import com.google.common.io.ByteStreams;
import no.difi.asic.api.AsicReaderLayer;
import no.difi.asic.api.SignatureVerifier;
import no.difi.asic.model.Container;
import no.difi.commons.asic.jaxb.cades.XAdESSignaturesType;
import no.difi.commons.asic.jaxb.xmldsig.SignatureType;
import no.difi.commons.asic.jaxb.xmldsig.SignedInfoType;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author erlend
 */
public class XadesSignatureVerifier extends XadesCommons implements SignatureVerifier {

    static final Pattern PATTERN_SIGNATURES =
            Pattern.compile("META-INF/signatures(.*)\\.xml", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean supports(String filename) {
        return PATTERN_SIGNATURES.matcher(filename).matches();
    }

    @Override
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteStreams.copy(asicReaderLayer.getContent(), byteArrayOutputStream);

        String xml = byteArrayOutputStream.toString()
                .replace("http://uri.etsi.org/02918/v1.1.1#", "http://uri.etsi.org/02918/v1.2.1#")
                .replace("http://uri.etsi.org/2918/v1.2.1#", "http://uri.etsi.org/02918/v1.2.1#");

        XAdESSignaturesType manifest;

        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            manifest = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes())), XAdESSignaturesType.class).getValue();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to read content as XML", e);
        }

        for (SignatureType signature : manifest.getSignature()) {
            SignedInfoType signedInfoType = signature.getSignedInfo();

            /*
            for (ReferenceType reference : signedInfoType.getReference()) {
                if (!reference.getURI().startsWith("#"))
                    manifestVerifier.update(reference.getURI(), null, reference.getDigestValue(), reference.getDigestMethod().getString(), null);
            }
            */
        }
    }
}
