package no.difi.asic.signature;

import com.google.common.io.ByteStreams;
import no.difi.asic.api.AsicWriterLayer;
import no.difi.asic.api.SignatureCreator;
import no.difi.asic.config.SignatureConfig;
import no.difi.asic.lang.AsicException;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.model.MimeType;
import no.difi.asic.util.MimeTypes;
import no.difi.commons.asic.jaxb.cades.ASiCManifestType;
import no.difi.commons.asic.jaxb.cades.DataObjectReferenceType;
import no.difi.commons.asic.jaxb.cades.SigReferenceType;
import no.difi.commons.asic.jaxb.xmldsig.DigestMethodType;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author erlend
 */
public class CadesSignatureCreator extends CadesCommons implements SignatureCreator {

    private static JcaDigestCalculatorProviderBuilder jcaDigestCalculatorProviderBuilder =
            new JcaDigestCalculatorProviderBuilder().setProvider(PROVIDER);

    private static final String MANIFEST_FILENAME = "META-INF/ASiCManifest-%s.xml";

    private static final String SIGNATURE_FILENAME = "META-INF/signature-%s.p7s";

    @Override
    public boolean supportsRootFile() {
        return true;
    }

    @Override
    public void create(AsicWriterLayer asicWriterLayer, Container container,
                       List<KeyStore.PrivateKeyEntry> keyEntries, SignatureConfig signatureConfig) throws IOException {
        try {
            for (KeyStore.PrivateKeyEntry keyEntry : keyEntries) {
                // Unique identifier
                String identifier = UUID.randomUUID().toString();

                JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder(
                        String.format("%swith%s", signatureConfig.getSignatureAlgorithm().getString(),
                                keyEntry.getPrivateKey().getAlgorithm())).setProvider(PROVIDER);

                DigestCalculatorProvider digestCalculatorProvider = jcaDigestCalculatorProviderBuilder.build();
                ContentSigner contentSigner = jcaContentSignerBuilder.build(keyEntry.getPrivateKey());
                SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider)
                        .build(contentSigner, (X509Certificate) keyEntry.getCertificate());

                CMSSignedDataGenerator dataGenerator = new CMSSignedDataGenerator();
                dataGenerator.addSignerInfoGenerator(signerInfoGenerator);
                dataGenerator.addCertificates(new JcaCertStore(
                        Collections.singletonList(keyEntry.getCertificate())));

                // Create manifest
                byte[] content = createManifest(asicWriterLayer, container, identifier, signatureConfig);

                // Create signed data
                CMSSignedData signedData = dataGenerator.generate(new CMSProcessableByteArray(content), false);

                // Write signature
                writeSignature(asicWriterLayer, identifier, signedData);
            }
        } catch (OperatorCreationException | CertificateEncodingException | CMSException e) {
            throw new AsicException("Error while generating signature.", e);
        }
    }

    private byte[] createManifest(AsicWriterLayer asicWriterLayer, Container container, String identifier,
                                  SignatureConfig signatureConfig) throws IOException {
        // Reference to signature file.
        SigReferenceType sigReferenceType = OBJECT_FACTORY.createSigReferenceType();
        sigReferenceType.setURI(String.format(SIGNATURE_FILENAME, identifier));
        sigReferenceType.setMimeType(SIGNATURE_MIME_TYPE);

        // Put together manifest
        ASiCManifestType aSiCManifestType = OBJECT_FACTORY.createASiCManifestType();
        aSiCManifestType.setSigReference(sigReferenceType);

        DigestMethodType digestMethodType = new DigestMethodType();
        digestMethodType.setAlgorithm(signatureConfig.getDataObjectAlgorithm()[0].getURI());

        // Add DataObjects
        for (DataObject dataObject : container.getDataObjects()) {
            DataObjectReferenceType dataObjectReferenceType = OBJECT_FACTORY.createDataObjectReferenceType();
            dataObjectReferenceType.setURI(dataObject.getFilename());
            dataObjectReferenceType.setMimeType(dataObject.getMimeType().getValue());
            dataObjectReferenceType.setDigestValue(dataObject.getHash()
                    .get(signatureConfig.getDataObjectAlgorithm()[0]));
            dataObjectReferenceType.setDigestMethod(digestMethodType);

            if (dataObject.getFilename().equals(container.getRootFile()))
                dataObjectReferenceType.setRootfile(true);

            aSiCManifestType.getDataObjectReference().add(dataObjectReferenceType);
        }

        // Create element
        JAXBElement<ASiCManifestType> jaxbElement = OBJECT_FACTORY.createASiCManifest(aSiCManifestType);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Write element to baos
        try (OutputStream outputStream =
                     asicWriterLayer.addContent(DataObject.Type.MANIFEST,
                             String.format(MANIFEST_FILENAME, identifier), MimeTypes.XML)) {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.marshal(jaxbElement, byteArrayOutputStream);

            // Write content to asic
            ByteStreams.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), outputStream);
        } catch (JAXBException e) {
            throw new AsicException(e.getMessage(), e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void writeSignature(AsicWriterLayer asicWriterLayer, String identifier, CMSSignedData cmsSignedData)
            throws IOException {
        try (OutputStream outputStream = asicWriterLayer.addContent(DataObject.Type.DETACHED_SIGNATURE,
                String.format(SIGNATURE_FILENAME, identifier), MimeType.forString(SIGNATURE_MIME_TYPE))) {
            ByteStreams.copy(new ByteArrayInputStream(cmsSignedData.getEncoded()), outputStream);
        }
    }
}
