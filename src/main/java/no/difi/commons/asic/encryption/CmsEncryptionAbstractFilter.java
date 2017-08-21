package no.difi.commons.asic.encryption;

import no.difi.commons.asic.api.EncryptionFilter;

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
