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
}
