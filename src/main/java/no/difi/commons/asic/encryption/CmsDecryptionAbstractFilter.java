package no.difi.commons.asic.encryption;

import no.difi.commons.asic.api.DecryptionFilter;

/**
 * @author erlend
 */
abstract class CmsDecryptionAbstractFilter implements DecryptionFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEncrypted(String filename) {
        return filename.endsWith(".p7m");
    }

    @Override
    public String parseFilename(String filename) {
        return filename.substring(0, filename.length() - 4);
    }
}
