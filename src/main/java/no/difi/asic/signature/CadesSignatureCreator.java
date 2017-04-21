package no.difi.asic.signature;

import com.google.common.io.ByteStreams;
import no.difi.asic.model.MimeType;
import no.difi.asic.api.AsicWriterLayer;
import no.difi.asic.api.SignatureCreator;
import no.difi.asic.config.SignatureConfig;
import no.difi.asic.lang.AsicExcepion;
import no.difi.asic.model.Container;
import no.difi.asic.model.DataObject;
import no.difi.asic.util.MimeTypes;
import no.difi.commons.asic.jaxb.cades.ASiCManifestType;
import no.difi.commons.asic.jaxb.cades.DataObjectReferenceType;
import no.difi.commons.asic.jaxb.cades.SigReferenceType;
import no.difi.commons.asic.jaxb.xmldsig.DigestMethodType;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
            new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME);

    private static final String MANIFEST_FILENAME = "META-INF/ASiCManifest-%s.xml";

    private static final String SIGNATURE_FILENAME = "META-INF/signature-%s.p7s";

    private static final String SIGNATURE_MIME_TYPE = "application/x-pkcs7-signature";

    @Override
    public boolean supportsRootFile() {
        return true;
    }

    @Override
    public void create(AsicWriterLayer asicWriterLayer, Container container, List<KeyStore.PrivateKeyEntry> keyEntries, SignatureConfig signatureConfig)
            throws IOException, AsicExcepion {
        try {
            for (KeyStore.PrivateKeyEntry keyEntry : keyEntries) {
                // Unique identifier
                String identifier = UUID.randomUUID().toString();

                JcaContentSignerBuilder jcaContentSignerBuilder =
                        new JcaContentSignerBuilder(String.format("SHA1with%s", keyEntry.getPrivateKey().getAlgorithm()))
                                .setProvider(BouncyCastleProvider.PROVIDER_NAME);

                DigestCalculatorProvider digestCalculatorProvider = jcaDigestCalculatorProviderBuilder.build();
                ContentSigner contentSigner = jcaContentSignerBuilder.build(keyEntry.getPrivateKey());
                SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider)
                        .build(contentSigner, (X509Certificate) keyEntry.getCertificate());

                CMSSignedDataGenerator cmsSignedDataGenerator = new CMSSignedDataGenerator();
                cmsSignedDataGenerator.addSignerInfoGenerator(signerInfoGenerator);
                cmsSignedDataGenerator.addCertificates(new JcaCertStore(Collections.singletonList(keyEntry.getCertificate())));

                // Create manifest
                byte[] content = createManifest(asicWriterLayer, container, identifier, signatureConfig);

                // Create signed data
                CMSSignedData cmsSignedData = cmsSignedDataGenerator.generate(new CMSProcessableByteArray(content), false);

                // Write signature
                writeSignature(asicWriterLayer, identifier, cmsSignedData);
            }
        } catch (OperatorCreationException | CertificateEncodingException | CMSException e) {
            throw new AsicExcepion("Error while generating signature.", e);
        }
    }

    private byte[] createManifest(AsicWriterLayer asicWriterLayer, Container container, String identifier,
                                  SignatureConfig signatureConfig)
            throws IOException, AsicExcepion {
        // Reference to signature file.
        SigReferenceType sigReferenceType = OBJECT_FACTORY.createSigReferenceType();
        sigReferenceType.setURI(String.format(SIGNATURE_FILENAME, identifier));
        sigReferenceType.setMimeType(SIGNATURE_MIME_TYPE);

        // Put together manifest
        ASiCManifestType aSiCManifestType = OBJECT_FACTORY.createASiCManifestType();
        aSiCManifestType.setSigReference(sigReferenceType);

        DigestMethodType digestMethodType = new DigestMethodType();
        digestMethodType.setAlgorithm(signatureConfig.getDataObjectAlgorithm().getURI());

        // Add DataObjects
        for (DataObject dataObject : container.getDataObjects()) {
            DataObjectReferenceType dataObjectReferenceType = OBJECT_FACTORY.createDataObjectReferenceType();
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

        // Write element to baos
        try (OutputStream outputStream =
                     asicWriterLayer.addContent(String.format(MANIFEST_FILENAME, identifier), MimeTypes.XML)) {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.marshal(jaxbElement, byteArrayOutputStream);

            // Write content to asic
            ByteStreams.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), outputStream);
        } catch (JAXBException e) {
            throw new AsicExcepion(e.getMessage(), e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void writeSignature(AsicWriterLayer asicWriterLayer, String identifier, CMSSignedData cmsSignedData)
            throws IOException, AsicExcepion {
        try (OutputStream outputStream = asicWriterLayer.addContent(String.format(SIGNATURE_FILENAME, identifier), MimeType.forString(SIGNATURE_MIME_TYPE))) {
            ByteStreams.copy(new ByteArrayInputStream(cmsSignedData.getEncoded()), outputStream);
        }
    }
}
