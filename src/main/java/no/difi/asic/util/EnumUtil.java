package no.difi.asic.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author erlend
 */
public class EnumUtil {

    public static <T, S extends Annotation> S getAnnotation(T key, Class<S> cls) {
        return getField(key).getAnnotation(cls);
    }

    public static <T> Field getField(T key) {
        try {
            return key.getClass().getField(((Enum) key).name());
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
