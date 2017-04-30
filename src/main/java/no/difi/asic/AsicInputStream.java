package no.difi.asic;

import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class AsicInputStream extends ZipInputStream {

    public AsicInputStream(InputStream in) {
        super(in);
    }

    @Override
    public ZipEntry getNextEntry() throws IOException {
        ZipEntry zipEntry = super.getNextEntry();

        if (zipEntry != null && zipEntry.getName().equals("mimetype")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteStreams.copy(this, baos);

            if (!AsicUtils.MIMETYPE_ASICE.equals(baos.toString()))
                throw new IllegalStateException("Content is not ASiC-E container.");

            // Fetch next
            zipEntry = super.getNextEntry();
        }

        return zipEntry;
    }
}
