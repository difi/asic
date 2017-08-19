package no.difi.asic.builder;

/**
 * @author erlend
 */
public interface BuildHandler<T> {

    T build(Properties properties);

}
