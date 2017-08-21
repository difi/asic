package no.difi.asic.test;

import no.difi.asic.builder.Builder;
import no.difi.asic.builder.Properties;

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
