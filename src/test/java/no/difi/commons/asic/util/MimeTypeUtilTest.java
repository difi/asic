package no.difi.commons.asic.util;

import no.difi.commons.asic.lang.MimeTypeException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author erlend
 */
public class MimeTypeUtilTest {

    @Test
    public void simple() throws MimeTypeException {
        Assert.assertEquals(MimeTypeUtil.detect("test.xml").getValue(), "application/xml");
        Assert.assertEquals(MimeTypeUtil.detect("test.asice").getValue(), "application/vnd.etsi.asic-e+zip");
        Assert.assertEquals(MimeTypeUtil.detect("test.html").getValue(), "text/html");
        Assert.assertEquals(MimeTypeUtil.detect("test.txt").getValue(), "text/plain");
        Assert.assertEquals(MimeTypeUtil.detect("test.png").getValue(), "image/png");
    }

    @Test(expectedExceptions = MimeTypeException.class)
    public void exceptionOnUnknown() throws MimeTypeException {
        MimeTypeUtil.detect("test.asics");
    }

    @Test
    public void constructorTest() {
        new MimeTypeUtil();
    }

}
