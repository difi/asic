package no.difi.asic.encryption;

import no.difi.asic.api.DecryptionFilter;

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
}
