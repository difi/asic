package no.difi.commons.asic.test;

import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.security.BCHelper;
import org.testng.annotations.Test;

/**
 * @author erlend
 */
public class BCHelperTest {

    @Test
    public void constructorTest() {
        new BCHelper();
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerUnknownAlgorithm() throws AsicException {
        BCHelper.createMessageDigest("UNKNOWN");
    }
}
