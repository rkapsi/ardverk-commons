package org.ardverk.enums;

public interface PrimitiveEnum {
    
    /**
     * An {@link PrimitiveEnum} that is backed by {@code int} values.
     */
    public static interface Int extends PrimitiveEnum {
        public int convert();
    }
}
