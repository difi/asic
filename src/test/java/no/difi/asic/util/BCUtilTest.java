package no.difi.asic.util;

import no.difi.asic.lang.AsicException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.Security;

/**
 * @author erlend
 */
public class BCUtilTest {

    @Test
    public void constructorTest() {
        new BCUtil();
    }

    @Test
    public void registerTest() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) != null)
            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);

        Assert.assertNull(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME));

        BCUtil.register();

        Assert.assertEquals(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME), BCUtil.PROVIDER);
    }

    @Test(expectedExceptions = AsicException.class)
    public void triggerUnknownAlgorithm() throws AsicException {
        BCUtil.createMessageDigest("UNKNOWN");
    }
}
