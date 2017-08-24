package no.difi.commons.asic.util;

import no.difi.commons.asic.lang.AsicException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

/**
 * @author erlend
 */
public class KeystoreUtilTest {

    @Test
    public void simple() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/keystore.jks")) {
            Assert.assertNotNull(KeyStoreUtil.load(inputStream, "changeit", "selfsigned", "changeit"));
        }
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerException() throws Exception {
        KeyStoreUtil.load(null, "changeit", "selfsigned", "changeit");
    }
}
