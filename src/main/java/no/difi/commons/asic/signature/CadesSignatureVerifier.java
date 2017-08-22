package no.difi.commons.asic.signature;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.Asic;
import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.api.MessageDigestAlgorithm;
import no.difi.commons.asic.api.SignatureVerifier;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.jaxb.cades.ASiCManifestType;
import no.difi.commons.asic.jaxb.cades.DataObjectReferenceType;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.FileCache;
import no.difi.commons.asic.model.MimeType;
import no.difi.commons.asic.util.BCUtil;
import no.difi.commons.asic.util.MimeTypes;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.util.Store;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

/**
 * @author erlend
 */
public class CadesSignatureVerifier extends CadesCommons implements SignatureVerifier {

    public static SignatureVerifier INSTANCE = new CadesSignatureVerifier();

    protected static final Pattern PATTERN_MANIFEST =
            Pattern.compile("META-INF/asicmanifest(.*)\\.xml", Pattern.CASE_INSENSITIVE);

    protected static final Pattern PATTERN_SIGNATURE =
            Pattern.compile("META-INF/signature(.*)\\.p7s", Pattern.CASE_INSENSITIVE);

    private static final JcaSimpleSignerInfoVerifierBuilder SIGNER_INFO_VERIFIER_BUILDER =
            new JcaSimpleSignerInfoVerifierBuilder().setProvider(BCUtil.PROVIDER);

    private static final JcaX509CertificateConverter CERTIFICATE_CONVERTER =
            new JcaX509CertificateConverter().setProvider(BCUtil.PROVIDER);


    @Override
    public boolean supports(String filename) {
        return PATTERN_MANIFEST.matcher(filename).matches() || PATTERN_SIGNATURE.matcher(filename).matches();
    }

    @Override
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container,
                       FileCache fileCache, Properties properties) throws IOException {
        if (PATTERN_MANIFEST.matcher(filename).matches()) {
            container.update(filename, DataObject.Type.MANIFEST, MimeTypes.XML);
            fileCache.put(filename, ByteStreams.toByteArray(asicReaderLayer.getContent()));
        } else {
            container.update(filename, DataObject.Type.DETACHED_SIGNATURE, MimeType.forString(SIGNATURE_MIME_TYPE));
            fileCache.put(filename, ByteStreams.toByteArray(asicReaderLayer.getContent()));
        }
    }

    @Override
    public void postHandler(Container container, FileCache fileCache, Properties properties)
            throws IOException {
        for (String filename : fileCache.keySet())
            if (PATTERN_MANIFEST.matcher(filename).matches())
                verifyManifest(filename, container, fileCache, properties);
    }

    private void verifyManifest(String filename, Container container, FileCache fileCache, Properties properties)
            throws IOException {
        ASiCManifestType aSiCManifest = parseManifest(fileCache.get(filename));

        // Verify signature.
        String sigReference = aSiCManifest.getSigReference().getURI();

        if (!fileCache.containsKey(sigReference))
            throw new AsicException(String.format("Unable to find signature file '%s'.", sigReference));

        X509Certificate certificate = validate(fileCache.get(filename), fileCache.get(sigReference));

        // Verify data objects.
        for (DataObjectReferenceType reference : aSiCManifest.getDataObjectReference()) {
            container.update(reference.getURI(), DataObject.Type.DATA,
                    MimeType.forString(reference.getMimeType()));

            container.verify(null, reference.getURI(),
                    MessageDigestAlgorithm.findByUri(
                            reference.getDigestMethod().getAlgorithm(),
                            properties.get(Asic.SIGNATURE_OBJECT_ALGORITHM)),
                    reference.getDigestValue());

            if (reference.isRootfile() != null && reference.isRootfile())
                container.setRootFile(reference.getURI());
        }
    }

    private X509Certificate validate(byte[] data, byte[] signature) throws AsicException {
        try {
            CMSSignedData cmsSignedData = new CMSSignedData(new CMSProcessableByteArray(data), signature);
            Store store = cmsSignedData.getCertificates();
            SignerInformationStore signerInformationStore = cmsSignedData.getSignerInfos();

            for (SignerInformation signerInformation : signerInformationStore.getSigners()) {
                X509CertificateHolder x509Certificate = (X509CertificateHolder) store.getMatches(signerInformation.getSID()).iterator().next();

                if (signerInformation.verify(SIGNER_INFO_VERIFIER_BUILDER.build(x509Certificate)))
                    return CERTIFICATE_CONVERTER.getCertificate(x509Certificate);
            }

            throw new AsicException("Unable to find valid certificate to verify signature.");
        } catch (Exception e) {
            throw new AsicException("Unable to verify signature.", e);
        }
    }

    private ASiCManifestType parseManifest(byte[] bytes) throws AsicException {
        try {
            String xml = new String(bytes)
                    .replace("http://uri.etsi.org/02918/v1.1.1#", "http://uri.etsi.org/02918/v1.2.1#")
                    .replace("http://uri.etsi.org/2918/v1.2.1#", "http://uri.etsi.org/02918/v1.2.1#");

            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return unmarshaller
                    .unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes())), ASiCManifestType.class)
                    .getValue();
        } catch (JAXBException e) {
            throw new AsicException(e.getMessage(), e);
        }
    }
}