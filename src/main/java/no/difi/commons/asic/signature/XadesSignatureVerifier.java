package no.difi.commons.asic.signature;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.Asic;
import no.difi.commons.asic.api.AsicReaderLayer;
import no.difi.commons.asic.api.MessageDigestAlgorithm;
import no.difi.commons.asic.api.SignatureVerifier;
import no.difi.commons.asic.builder.Properties;
import no.difi.commons.asic.jaxb.cades.XAdESSignaturesType;
import no.difi.commons.asic.jaxb.xmldsig.ReferenceType;
import no.difi.commons.asic.jaxb.xmldsig.SignatureType;
import no.difi.commons.asic.jaxb.xmldsig.SignedInfoType;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.Container;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.FileCache;
import no.difi.commons.asic.model.MimeType;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * @author erlend
 */
public class XadesSignatureVerifier extends XadesCommons implements SignatureVerifier {

    public static final SignatureVerifier INSTANCE = new XadesSignatureVerifier();

    private static final Pattern PATTERN_SIGNATURES =
            Pattern.compile("META-INF/signatures(.*)\\.xml", Pattern.CASE_INSENSITIVE);

    private static final XMLSignatureFactory xmlSignatureFactory;

    public static final DocumentBuilderFactory documentBuilderFactory;

    static {
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setCoalescing(true);
            documentBuilderFactory.setIgnoringComments(true);
            documentBuilderFactory.setNamespaceAware(true);

            xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM", "XMLDSig");
        } catch (NoSuchProviderException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(String filename) {
        return PATTERN_SIGNATURES.matcher(filename).matches();
    }

    @Override
    public void handle(AsicReaderLayer asicReaderLayer, String filename, Container container,
                       FileCache fileCache, Properties properties) throws IOException {

        byte[] bytes = ByteStreams.toByteArray(asicReaderLayer.getContent());

        X509Certificate certificate;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(bytes));

            certificate = verify(document, properties);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Unable to read content as XML", e);
        }

        XAdESSignaturesType manifest;

        try {
            byte[] fixed = new String(bytes)
                    .replace("http://uri.etsi.org/02918/v1.1.1#", "http://uri.etsi.org/02918/v1.2.1#")
                    .replace("http://uri.etsi.org/2918/v1.2.1#", "http://uri.etsi.org/02918/v1.2.1#")
                    .getBytes();

            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            manifest = unmarshaller
                    .unmarshal(new StreamSource(new ByteArrayInputStream(fixed)), XAdESSignaturesType.class)
                    .getValue();
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to read content as XML", e);
        }

        for (SignatureType signature : manifest.getSignature()) {
            SignedInfoType signedInfoType = signature.getSignedInfo();

            for (ReferenceType reference : signedInfoType.getReference()) {
                if (!reference.getURI().startsWith("#")) {
                    container.update(reference.getURI(), DataObject.Type.DATA, MimeType.APPLICATION_XML);

                    /*
                    container.verify(null, reference.getURI(),
                            MessageDigestAlgorithm.findByUri(reference.getDigestMethod().getAlgorithm(), properties.get(Asic.SIGNATURE_OBJECT_ALGORITHM)),
                            reference.getDigestValue());
                            */
                }
            }
        }
    }

    public X509Certificate verify(Document document, Properties properties) throws AsicException {
        try {
            NodeList nl = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            if (nl.getLength() == 0)
                throw new AsicException("Cannot find Signature element");

            X509KeySelector keySelector = new X509KeySelector(properties);
            DOMValidateContext validateContext = new DOMValidateContext(keySelector, nl.item(0));

            XMLSignature signature = xmlSignatureFactory.unmarshalXMLSignature(validateContext);

            try {
                Class drCls = Class.forName("org.jcp.xml.dsig.internal.dom.DOMReference");

                for (Object o : signature.getSignedInfo().getReferences()) {
                    String id = (String) drCls.getMethod("getId").invoke(o);

                    // if (id != null) {
                    Field field = drCls.getDeclaredField("validated");
                    field.setAccessible(true);
                    field.setBoolean(o, true);

                    field = drCls.getDeclaredField("validationStatus");
                    field.setAccessible(true);
                    field.setBoolean(o, true);
                    // }
                }
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new AsicException("Unable to mark reference as validated", e);
            }

            if (!signature.validate(validateContext))
                throw new AsicException("Signature failed.");

            return keySelector.getCertificate();
        } catch (XMLSignatureException | MarshalException e) {
            throw new AsicException("Unable to verify document signature.", e);
        }
    }

    private class X509KeySelector extends KeySelector {

        private X509Certificate certificate;

        private final Properties properties;

        public X509KeySelector(Properties properties) {
            this.properties = properties;
        }

        @Override
        public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method,
                                        XMLCryptoContext context) throws KeySelectorException {
            Iterator ki = keyInfo.getContent().iterator();
            while (ki.hasNext()) {
                XMLStructure info = (XMLStructure) ki.next();
                if (!(info instanceof X509Data))
                    continue;

                X509Data x509Data = (X509Data) info;
                Iterator xi = x509Data.getContent().iterator();
                while (xi.hasNext()) {
                    Object o = xi.next();
                    if (!(o instanceof X509Certificate))
                        continue;

                    this.certificate = (X509Certificate) o;
                    final PublicKey key = certificate.getPublicKey();

                    // Make sure the algorithm is compatible with the method.
                    if (MessageDigestAlgorithm.findByUri(method.getAlgorithm(), properties.get(Asic.SIGNATURE_ALGORITHM)) == null)
                        throw new KeySelectorException(String.format("Invalid signature algorithm '%s' is used.", method.getAlgorithm()));

                    return () -> key;
                }
            }

            throw new KeySelectorException("No key found!");
        }

        public X509Certificate getCertificate() {
            return certificate;
        }
    }
}
