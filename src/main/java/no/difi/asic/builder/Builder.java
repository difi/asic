package no.difi.asic.builder;

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

    public Builder(BuildHandler<T> buildHandler) {
        this(buildHandler, new HashMap<>());
    }

    public Builder(Properties properties, BuildHandler<T> buildHandler) {
        this(buildHandler, properties.map);
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
