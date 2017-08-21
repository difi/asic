package no.difi.asic.encryption;

import no.difi.asic.api.EncryptionFilter;

/**
 * @author erlend
 */
abstract class CmsEncryptionAbstractFilter implements EncryptionFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    public String filename(String filename) {
        return String.format("%s.p7m", filename);
    }

}
