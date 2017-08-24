package no.difi.commons.asic.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author erlend
 */
public interface FileCache extends Map<String, byte[]> {

    static FileCache create() {
        return new DefaultFileCache();
    }

    class DefaultFileCache extends HashMap<String, byte[]> implements FileCache {

        private DefaultFileCache() {
            super();
        }

    }
}
