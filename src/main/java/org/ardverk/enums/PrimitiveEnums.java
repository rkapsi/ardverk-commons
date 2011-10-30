package org.ardverk.enums;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;

public class PrimitiveEnums {
    
    public static class Int {
        
        private static final ConcurrentHashMap<Class<?>, Enum<?>[]> TABLE 
            = new ConcurrentHashMap<Class<?>, Enum<?>[]>();
        
        /**
         * 
         */
        @SuppressWarnings("unchecked")
        public static <V extends Enum<V> & PrimitiveEnum.Int> V[] of(Class<V> type) {
            V[] values = (V[])TABLE.get(type);
            if (values == null) {
                values = createTable(type);
                Enum<?>[] existing = TABLE.putIfAbsent(type, values);
                if (existing != null) {
                    values = (V[])existing;
                }
            }
            return values;
        }
        
        /**
         * 
         */
        public static <V extends Enum<V> & PrimitiveEnum.Int> V valueOf(Class<V> type, int key) {
            V[] values = of(type);
            V value = values[(key & Integer.MAX_VALUE) % values.length];
            
            if (value != null && value.convert() == key) {
                return value;
            }
            
            throw new IllegalArgumentException("key=" + key);
        }
        
        /**
         * 
         */
        @SuppressWarnings("unchecked")
        private static <V extends Enum<V> & PrimitiveEnum.Int> V[] createTable(Class<V> type) {
    
            V[] values = type.getEnumConstants();
            
            Class<?> componentType = values.getClass().getComponentType();
            V[] dst = (V[])Array.newInstance(componentType, values.length);
    
            for (V value : values) {
                int index = (value.convert() & Integer.MAX_VALUE) % values.length;
                
                V existing = dst[index];
                if (existing != null) {
                    throw new IllegalStateException("Conflict: " + value + " vs. " + existing);
                }
                
                dst[index] = value;
            }
    
            return dst;
        }
    
        private Int() {}
    }
    
    
    private PrimitiveEnums() {}
}
