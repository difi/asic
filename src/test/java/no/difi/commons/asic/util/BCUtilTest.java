package no.difi.commons.asic.util;

import no.difi.commons.asic.lang.AsicException;
import org.testng.annotations.Test;

/**
 * @author erlend
 */
public class BCUtilTest {

    @Test
    public void constructorTest() {
        new BCUtil();
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerUnknownAlgorithm() throws AsicException {
        BCUtil.createMessageDigest("UNKNOWN");
    }
}
