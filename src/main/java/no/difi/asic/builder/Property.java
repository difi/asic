package no.difi.asic.builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author erlend
 */
public class Property<T> {

    private final T defaultValue;

    public static <T> Property<T> create() {
        return new Property<>(null);
    }

    public static <T> Property<T> create(T defaultValue) {
        return new Property<>(defaultValue);
    }

    @SafeVarargs
    public static <T> Property<List<T>> createList(T... defaultValues) {
        return new Property<>(Collections.unmodifiableList(Arrays.asList(defaultValues)));
    }

    private Property(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

}
