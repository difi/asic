package no.difi.commons.asic.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author erlend
 */
public class Builder<T> {

    protected BuildHandler<T> buildHandler;

    protected Map<Property<?>, Object> map;

    public static <T> Builder<T> of(BuildHandler<T> buildHandler) {
        return new Builder<>(buildHandler, new HashMap<>());
    }

    public static <T> Builder<T> of(Properties properties, BuildHandler<T> buildHandler) {
        return new Builder<>(buildHandler, properties.map);
    }

    public static Builder<Properties> raw() {
        return new Builder<>(p -> p, new HashMap<>());
    }

    private Builder(BuildHandler<T> buildHandler, Map<Property<?>, Object> map) {
        this.buildHandler = buildHandler;
        this.map = map;
    }

    public <S> Builder<T> set(Property<S> property, S value) {
        Map<Property<?>, Object> map = new HashMap<>(this.map);
        map.put(property, value);

        return new Builder<>(buildHandler, map);
    }

    public <S> Builder<T> set(Property<List<S>> property, S... value) {
        Map<Property<?>, Object> map = new HashMap<>(this.map);
        map.put(property, Arrays.asList(value));

        return new Builder<>(buildHandler, map);
    }

    public T build() {
        return buildHandler.build(new Properties(map));
    }
}
