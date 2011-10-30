package org.ardverk.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 */
public class ObjectEnums {

    private static final ConcurrentHashMap<Class<?>, Map<?, ?>> TABLE 
        = new ConcurrentHashMap<Class<?>, Map<?, ?>>();
    
    /**
     * Returns a reverse lookup {@link Map} for the given {@link Class}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V extends Enum<V> & ObjectEnum<K>> Map<K, V> of(Class<V> type) {
        Map<K, V> map = (Map<K, V>)TABLE.get(type);
        if (map == null) {
            map = createTable(type);
            Map<?, ?> existing = TABLE.putIfAbsent(type, map);
            if (existing != null) {
                map = (Map<K, V>)existing;
            }
        }
        return map;
    }
    
    /**
     * 
     */
    public static <K, V extends Enum<V> & ObjectEnum<K>> V valueOf(Class<V> type, K key) {
        return of(type).get(key);
    }

    /**
     * Creates and returns a reverse lookup {@link Map} for the given {@link Class}.
     * 
     * @see #of(Class)
     */
    private static <K, V extends Enum<V> & ObjectEnum<K>> Map<K, V> createTable(Class<V> type) {

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

    private ObjectEnums() {}
}
