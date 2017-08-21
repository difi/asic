package no.difi.commons.asic.code;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author erlend
 */
public class MessageDigestAlgorithmsTest {

    @Test
    public void valueOfTests() {
        Assert.assertEquals(MessageDigestAlgorithms.SHA1, MessageDigestAlgorithms.valueOf("SHA1"));
        Assert.assertEquals(MessageDigestAlgorithms.SHA256, MessageDigestAlgorithms.valueOf("SHA256"));
    }

    @Test
    public void findByUriTests() {
        Assert.assertEquals(
                MessageDigestAlgorithms.SHA256,
                MessageDigestAlgorithms.findByUri("http://www.w3.org/2000/09/xmldsig#sha256"));
        Assert.assertEquals(
                MessageDigestAlgorithms.SHA256,
                MessageDigestAlgorithms.findByUri("http://www.w3.org/2001/04/xmlenc#sha256"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void triggerExceptionInFindByUri() {
        MessageDigestAlgorithms.findByUri("http://www.w3.org/2001/04/xmlenc#sha255");
    }
}
