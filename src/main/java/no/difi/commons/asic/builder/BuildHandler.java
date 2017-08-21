package no.difi.commons.asic.builder;

/**
 * @author erlend
 */
public interface BuildHandler<T> {

    T build(Properties properties);

}
