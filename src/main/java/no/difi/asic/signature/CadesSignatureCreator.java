package no.difi.asic.signature;

import no.difi.asic.api.SignatureCreator;
import no.difi.asic.config.SignatureConfig;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.commons.asic.jaxb.cades.ASiCManifestType;
import no.difi.commons.asic.jaxb.cades.DataObjectReferenceType;
import no.difi.commons.asic.jaxb.cades.SigReferenceType;
import no.difi.commons.asic.jaxb.xmldsig.DigestMethodType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.UUID;

/**
 * @author erlend
 */
public class CadesSignatureCreator extends CadesCommons implements SignatureCreator {

    private static final String MANIFEST_FILENAME = "META-INF/ASiCManifest-%s.xml";

    private static final String SIGNATURE_FILENAME = "META-INF/signature-%s.p7s";

    private static final String SIGNATURE_MIMETYPE = "application/x-pkcs7-signature";

    @Override
    public boolean supportsRootFile() {
        return true;
    }

    @Override
    public void create(Container container, KeyStore.PrivateKeyEntry privateKeyEntry, SignatureConfig signatureConfig)
            throws IOException, AsicExcepion {
        String identifier = UUID.randomUUID().toString();

        createManifest(container, identifier, signatureConfig);
        createSignature(privateKeyEntry, identifier, signatureConfig);
    }

    private void createManifest(Container container, String identifier, SignatureConfig signatureConfig)
            throws AsicExcepion {
        // Reference to signature file.
        SigReferenceType sigReferenceType = new SigReferenceType();
        sigReferenceType.setURI(String.format(SIGNATURE_FILENAME, identifier));
        sigReferenceType.setMimeType(SIGNATURE_MIMETYPE);

        // Put together manifest
        ASiCManifestType aSiCManifestType = new ASiCManifestType();
        aSiCManifestType.setSigReference(sigReferenceType);

        DigestMethodType digestMethodType = new DigestMethodType();
        digestMethodType.setAlgorithm(signatureConfig.getDataObjectAlgorithm().getURI());

        // Add DataObjects
        for (DataObject dataObject : container.getDataObjects()) {
            DataObjectReferenceType dataObjectReferenceType = new DataObjectReferenceType();
            dataObjectReferenceType.setURI(dataObject.getFilename());
            dataObjectReferenceType.setMimeType(dataObject.getMimeType());
            dataObjectReferenceType.setDigestValue(dataObject.getHash());
            dataObjectReferenceType.setDigestMethod(digestMethodType);

            if (dataObject.getFilename().equals(container.getRootFile()))
                dataObjectReferenceType.setRootfile(true);

            aSiCManifestType.getDataObjectReference().add(dataObjectReferenceType);
        }

        // Create element
        JAXBElement<ASiCManifestType> jaxbElement = OBJECT_FACTORY.createASiCManifest(aSiCManifestType);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.marshal(jaxbElement, byteArrayOutputStream);
        } catch (JAXBException e) {
            throw new AsicExcepion(e.getMessage(), e);
        }

        System.out.println(byteArrayOutputStream.toString());
    }

    private void createSignature(KeyStore.PrivateKeyEntry privateKeyEntry, String identifier,
                                 SignatureConfig signatureConfig) throws AsicExcepion {

    }
}
