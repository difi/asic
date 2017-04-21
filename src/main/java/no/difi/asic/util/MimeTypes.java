package no.difi.asic.util;

import no.difi.asic.model.MimeType;
import no.difi.asic.lang.MimeTypeException;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author erlend
 */
public class MimeTypes {

    public static final MimeType XML = MimeType.forString("application/xml");

    private static FileNameMap fileNameMap = URLConnection.getFileNameMap();

    public static MimeType detect(String filename) throws MimeTypeException {
        try {
            // Use Files to find content type
            String mimeType = Files.probeContentType(Paths.get(filename));

            // Use URLConnection to find content type
            if (mimeType == null)
                mimeType = fileNameMap.getContentTypeFor(filename);

            // Throw exception if content type is not detected
            if (mimeType == null)
                throw new MimeTypeException(String.format("Unable to determine MIME type of %s", filename));

            return MimeType.forString(mimeType);
        } catch (IOException e) {
            throw new MimeTypeException(e.getMessage(), e);
        }
    }
}
