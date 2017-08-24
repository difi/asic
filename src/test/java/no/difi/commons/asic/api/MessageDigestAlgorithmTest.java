package no.difi.commons.asic.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author erlend
 */
public class MessageDigestAlgorithmTest {

    @Test
    public void findByUriTest() {
        Assert.assertEquals(
                MessageDigestAlgorithm.findByUri(MessageDigestAlgorithm.SHA1.getURI(), Arrays.asList(
                        MessageDigestAlgorithm.SHA256, MessageDigestAlgorithm.SHA1
                )),
                MessageDigestAlgorithm.SHA1
        );

        Assert.assertNull(
                MessageDigestAlgorithm.findByUri("invalid", Arrays.asList(
                        MessageDigestAlgorithm.SHA256, MessageDigestAlgorithm.SHA1
                ))
        );

        Assert.assertNull(
                MessageDigestAlgorithm.findByUri(null, Arrays.asList(
                        MessageDigestAlgorithm.SHA256, MessageDigestAlgorithm.SHA1
                ))
        );
    }

}
