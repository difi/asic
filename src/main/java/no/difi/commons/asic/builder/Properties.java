package no.difi.commons.asic.builder;

import java.util.Map;

/**
 * @author erlend
 */
public class Properties {

    protected Map<Property<?>, Object> map;

    protected Properties(Map<Property<?>, Object> map) {
        this.map = map;
    }

    public <S> S get(Property<S> property) {
        return map.containsKey(property) ? (S) map.get(property) : property.getDefaultValue();
    }
}
