package no.difi.commons.asic.api;

import no.difi.commons.asic.builder.Properties;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public interface DecryptionFilter {

    DecryptionFilter NOOP = new NoopDecryptionFilter();

    boolean isEncrypted(String filename);

    String parseFilename(String filename);

    InputStream createFilter(InputStream inputStream, Properties properties) throws IOException;



    class NoopDecryptionFilter implements DecryptionFilter {

        private NoopDecryptionFilter() {
            // No action.
        }

        @Override
        public boolean isEncrypted(String filename) {
            return false;
        }

        @Override
        public String parseFilename(String filename) {
            return filename;
        }

        @Override
        public InputStream createFilter(InputStream inputStream, Properties properties) throws IOException {
            return inputStream;
        }
    }

}