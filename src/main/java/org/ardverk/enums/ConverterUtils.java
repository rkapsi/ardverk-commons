package org.ardverk.enums;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class ConverterUtils {

    /**
     * 
     */
    public static <K, V extends Enum<V> & Converter<K>> V valueOf(Class<V> type, K value) {
        
        String name = "valueOf";
        if (value instanceof String) {
            name = "from";
        }

        try {
            Method method = type.getMethod(name, value.getClass());
            return type.cast(method.invoke(null, value));
        } catch (NoSuchMethodException err) {
            throw new IllegalArgumentException("NoSuchMethodException", err);
        } catch (IllegalAccessException err) {
            throw new IllegalArgumentException("IllegalAccessException", err);
        } catch (InvocationTargetException err) {
            throw new IllegalArgumentException("InvocationTargetException", err);
        }
    }

    /**
     * 
     */
    public static <K, V extends Enum<V> & Converter<K>> Map<K, V> of(Class<V> type) {

        V[] values = type.getEnumConstants();

        Map<K, V> map = new HashMap<K, V>(values.length) {

            private static final long serialVersionUID = -1567428946620335533L;

            @Override
            public V get(Object key) {
                V value = super.get(key);
                if (value == null) {
                    throw new IllegalStateException("key=" + key);
                }
                return value;
            }
        };

        for (V value : values) {
            V existing = map.put(value.convert(), value);
            if (existing != null) {
                throw new IllegalStateException("Conflict: " + value + " vs. " + existing);
            }
        }

        return Collections.unmodifiableMap(map);
    }

    private ConverterUtils() {}
}
