package no.difi.commons.asic.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author erlend
 */
public class Builder<T, E extends Exception> {

    protected BuildHandler<T, E> buildHandler;

    protected Map<Property<?>, Object> map;

    public static <T, E extends Exception> Builder<T, E> of(BuildHandler<T, E> buildHandler) {
        return new Builder<>(buildHandler, new HashMap<>());
    }

    public static <T, E extends Exception> Builder<T, E> of(Properties properties, BuildHandler<T, E> buildHandler) {
        return new Builder<>(buildHandler, properties.map);
    }

    public static Builder<Properties, RuntimeException> raw() {
        return new Builder<>(p -> p, new HashMap<>());
    }

    private Builder(BuildHandler<T, E> buildHandler, Map<Property<?>, Object> map) {
        this.buildHandler = buildHandler;
        this.map = map;
    }

    public Builder<T, E> set(Properties properties) {
        Map<Property<?>, Object> map = new HashMap<>(this.map);
        map.putAll(properties.map);

        return new Builder<>(buildHandler, map);
    }

    public <S> Builder<T, E> set(Property<S> property, S value) {
        Map<Property<?>, Object> map = new HashMap<>(this.map);
        map.put(property, value);

        return new Builder<>(buildHandler, map);
    }

    @SafeVarargs
    public final <S> Builder<T, E> set(Property<List<S>> property, S... value) {
        Map<Property<?>, Object> map = new HashMap<>(this.map);
        map.put(property, Arrays.asList(value));

        return new Builder<>(buildHandler, map);
    }

    public T build() throws E {
        return buildHandler.build(new Properties(map));
    }
}
