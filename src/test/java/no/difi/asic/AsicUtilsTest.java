package no.difi.asic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class AsicUtilsTest {

    private static Logger log = LoggerFactory.getLogger(AsicUtilsTest.class);

    private AsicReaderFactory asicReaderFactory = AsicReaderFactory.newFactory();
    private SignatureHelper signatureHelper = new SignatureHelper(getClass().getResourceAsStream("/kontaktinfo-client-test.jks"), "changeit", null, "changeit");

    private String fileContent1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam arcu eros, fermentum vel molestie ut, sagittis vel velit.";
    private String fileContent2 = "Fusce eu risus ipsum. Sed mattis laoreet justo. Fusce nisi magna, posuere ac placerat tincidunt, dignissim non lacus.";

    @Test
    public void validatePatterns() {
        assertTrue(AsicUtils.PATTERN_CADES_MANIFEST.matcher("META-INF/asicmanifest.xml").matches());
        assertTrue(AsicUtils.PATTERN_CADES_MANIFEST.matcher("META-INF/ASICMANIFESTt.xml").matches());
        assertTrue(AsicUtils.PATTERN_CADES_MANIFEST.matcher("META-INF/asicmanifest1.xml").matches());
        assertFalse(AsicUtils.PATTERN_CADES_MANIFEST.matcher("META-INF/asicmanifesk.xml").matches());

        assertTrue(AsicUtils.PATTERN_CADES_SIGNATURE.matcher("META-INF/signature.p7s").matches());
        assertTrue(AsicUtils.PATTERN_CADES_SIGNATURE.matcher("META-INF/SIGNATURE.p7s").matches());
        assertTrue(AsicUtils.PATTERN_CADES_SIGNATURE.matcher("META-INF/signature-cafecafe.p7s").matches());
        assertFalse(AsicUtils.PATTERN_CADES_SIGNATURE.matcher("META-INF/signatures.xml").matches());

        assertTrue(AsicUtils.PATTERN_XADES_SIGNATURES.matcher("META-INF/signatures.xml").matches());
        assertTrue(AsicUtils.PATTERN_XADES_SIGNATURES.matcher("META-INF/SIGNATURES.xml").matches());
        assertTrue(AsicUtils.PATTERN_XADES_SIGNATURES.matcher("META-INF/signatures1.xml").matches());
        assertFalse(AsicUtils.PATTERN_XADES_SIGNATURES.matcher("META-INF/signature.xml").matches());

        assertTrue(AsicUtils.PATTERN_EXTENSION_ASICE.matcher("mycontainer.asice").matches());
        assertFalse(AsicUtils.PATTERN_EXTENSION_ASICE.matcher("mycontainer.asice3").matches());
        assertFalse(AsicUtils.PATTERN_EXTENSION_ASICE.matcher("file://c/Users/skrue/mycontainer.asice3").matches());
        assertTrue(AsicUtils.PATTERN_EXTENSION_ASICE.matcher("mycontainer.sce").matches());
    }
}
