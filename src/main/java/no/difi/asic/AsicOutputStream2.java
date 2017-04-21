package no.difi.asic;

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

    public AsicOutputStream2(OutputStream outputStream) {
        zipOutputStream = new ZipOutputStream(outputStream);

        zipOutputStream.setComment("mimetype=" + AsicOutputStream.APPLICATION_VND_ETSI_ASIC_E_ZIP);
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
