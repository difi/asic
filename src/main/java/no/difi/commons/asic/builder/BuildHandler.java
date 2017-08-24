package no.difi.commons.asic.builder;

/**
 * @author erlend
 */
public interface BuildHandler<T, E extends Exception> {

    T build(Properties properties) throws E;

}
