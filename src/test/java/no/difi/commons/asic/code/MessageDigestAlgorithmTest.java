package no.difi.commons.asic.code;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author erlend
 */
public class MessageDigestAlgorithmTest {

    @Test
    public void valueOfTests() {
        Assert.assertEquals(MessageDigestAlgorithm.SHA1, MessageDigestAlgorithm.valueOf("SHA1"));
        Assert.assertEquals(MessageDigestAlgorithm.SHA256, MessageDigestAlgorithm.valueOf("SHA256"));
    }

    @Test
    public void findByUriTests() {
        Assert.assertEquals(
                MessageDigestAlgorithm.SHA256,
                MessageDigestAlgorithm.findByUri("http://www.w3.org/2000/09/xmldsig#sha256"));
        Assert.assertEquals(
                MessageDigestAlgorithm.SHA256,
                MessageDigestAlgorithm.findByUri("http://www.w3.org/2001/04/xmlenc#sha256"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void triggerExceptionInFindByUri() {
        MessageDigestAlgorithm.findByUri("http://www.w3.org/2001/04/xmlenc#sha255");
    }
}
