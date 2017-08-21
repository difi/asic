package no.difi.commons.asic.test;

import no.difi.commons.asic.api.AsicWriterLayer;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.model.DataObject;
import no.difi.commons.asic.model.MimeType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author erlend
 */
public class TestWriterLayer implements AsicWriterLayer {

    private Map<String, ByteArrayOutputStream> streamMap = new HashMap<>();

    @Override
    public OutputStream addContent(DataObject.Type type, String filename, MimeType mimeType)
            throws IOException, AsicException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        streamMap.put(filename, byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    public Set<String> getFiles() {
        return streamMap.keySet();
    }

    public byte[] getContent(String filename) {
        return streamMap.get(filename).toByteArray();
    }

    @Override
    public void close() throws IOException {
        // No action.
    }
}
