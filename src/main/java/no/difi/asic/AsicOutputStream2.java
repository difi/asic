package no.difi.asic;

import com.google.common.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Simple encapsulation of ZipOutputStream.
 *
 * @author erlend
 */
class AsicOutputStream2 extends OutputStream {

    private ZipOutputStream zipOutputStream;

    public AsicOutputStream2(OutputStream outputStream) throws IOException {
        // Initiate ZIP stream
        zipOutputStream = new ZipOutputStream(outputStream);

        // Write comment
        zipOutputStream.setComment("mimetype=" + AsicOutputStream.APPLICATION_VND_ETSI_ASIC_E_ZIP);

        // Write "mimetype" file
        zipOutputStream.putNextEntry(new ZipEntry("mimetype"));
        ByteStreams.copy(new ByteArrayInputStream(AsicOutputStream.APPLICATION_VND_ETSI_ASIC_E_ZIP.getBytes()), zipOutputStream);
        zipOutputStream.closeEntry();
    }

    public void nextEntry(String filename) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filename);

        zipOutputStream.putNextEntry(zipEntry);
    }

    public void closeEntry() throws IOException {
        zipOutputStream.closeEntry();
    }

    @Override
    public void write(int b) throws IOException {
        zipOutputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        zipOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.close();
    }
}
