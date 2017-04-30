package no.difi.asic.signature;

import no.difi.asic.api.SignatureVerifier;
import no.difi.commons.asic.jaxb.cades.XAdESSignaturesType;
import no.difi.commons.asic.jaxb.xades.ObjectFactory;
import no.difi.commons.asic.jaxb.xmldsig.SignatureType;
import no.difi.commons.asic.jaxb.xmldsig.SignedInfoType;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

/**
 * @author erlend
 */
public class XadesSignatureVerifier extends XadesCommons implements SignatureVerifier {

    private static ObjectFactory objectFactory1_2 = new ObjectFactory();

    private static no.difi.commons.asic.jaxb.cades.ObjectFactory objectFactory1_3 = new no.difi.commons.asic.jaxb.cades.ObjectFactory();

    @Override
    public boolean isSignatureFile(String filename) {
        return false;
    }


    @SuppressWarnings("unchecked")
    public static void extractAndVerify(String xml) {
        // Updating namespace
        xml = xml.replace("http://uri.etsi.org/02918/v1.1.1#", "http://uri.etsi.org/02918/v1.2.1#");
        xml = xml.replace("http://uri.etsi.org/2918/v1.2.1#", "http://uri.etsi.org/02918/v1.2.1#");

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
                    manifestVerifier.update(reference.getURI(), null, reference.getDigestValue(), reference.getDigestMethod().getAlgorithm(), null);
            }
            */
        }
    }
}
