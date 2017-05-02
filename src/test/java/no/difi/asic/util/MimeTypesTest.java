package no.difi.asic.util;

import no.difi.asic.lang.MimeTypeException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author erlend
 */
public class MimeTypesTest {

    @Test
    public void simple() throws MimeTypeException {
        Assert.assertEquals(MimeTypes.detect("test.xml").getValue(), "text/xml");
        Assert.assertEquals(MimeTypes.detect("test.html").getValue(), "text/html");
        Assert.assertEquals(MimeTypes.detect("test.txt").getValue(), "text/plain");
        Assert.assertEquals(MimeTypes.detect("test.png").getValue(), "image/png");
    }

    @Test(expectedExceptions = MimeTypeException.class)
    public void exceptionOnUnknown() throws MimeTypeException {
        MimeTypes.detect("test.asice");
    }

    @Test
    public void constructorTest() {
        new MimeTypes();
    }

}
