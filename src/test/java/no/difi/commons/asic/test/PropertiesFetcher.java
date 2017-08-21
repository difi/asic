package no.difi.commons.asic.test;

import no.difi.commons.asic.builder.Builder;
import no.difi.commons.asic.builder.Properties;

/**
 * @author erlend
 */
public class PropertiesFetcher {

    private Properties properties;

    public static Builder<PropertiesFetcher> builder() {
        return new Builder<>(PropertiesFetcher::new);
    }

    public PropertiesFetcher(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}
