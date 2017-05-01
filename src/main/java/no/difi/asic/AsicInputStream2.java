package no.difi.asic;

import com.google.common.io.ByteStreams;
import no.difi.asic.io.UnclosableInputStream;
import no.difi.asic.lang.AsicException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author erlend
 */
class AsicInputStream2 extends InputStream {

    private ZipInputStream zipInputStream;

    private String currentFilename;

    public AsicInputStream2(InputStream inputStream) {
        this.zipInputStream = new ZipInputStream(inputStream);
    }

    public String nextEntry() throws IOException, AsicException {
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        if (zipEntry == null)
            return null;

        currentFilename = zipEntry.getName();

        // Return filename if file is not the mimetype file.
        if (!"mimetype".equals(zipEntry.getName()))
            return zipEntry.getName();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteStreams.copy(zipInputStream, byteArrayOutputStream);
        closeEntry();

        if (!byteArrayOutputStream.toString().equals(AsicOutputStream.APPLICATION_VND_ETSI_ASIC_E_ZIP))
            throw new AsicException(String.format("Detected invalid mimetype '%s'.", byteArrayOutputStream.toString()));

        // Return next.
        return nextEntry();
    }

    public String getCurrentFilename() {
        return currentFilename;
    }

    public InputStream getContent() {
        return zipInputStream;
    }

    public void closeEntry() throws IOException {
        zipInputStream.closeEntry();
    }

    @Override
    public int read() throws IOException {
        return zipInputStream.read();
    }

    @Override
    public void close() throws IOException {
        zipInputStream.close();
    }
}
