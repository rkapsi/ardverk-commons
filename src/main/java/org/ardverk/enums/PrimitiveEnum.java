package org.ardverk.enums;

/**
 * An interface for {@link Enum}'s that provide an alternative value
 * of themselves.
 * 
 * @see PrimitiveEnum.Int
 */
public interface PrimitiveEnum {
    
    /**
     * An {@link PrimitiveEnum} that is backed by {@code int} values.
     */
    public static interface Int extends PrimitiveEnum {
        
        /**
         * Returns the {@link Enum}'s alternative {@code int} value.
         */
        public int convert();
    }
}
