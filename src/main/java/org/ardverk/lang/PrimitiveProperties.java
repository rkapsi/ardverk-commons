package org.ardverk.lang;

/**
 * 
 */
public interface PrimitiveProperties<K> {

    /**
     * 
     */
    public boolean getBoolean(K key);
    
    /**
     * 
     */
    public boolean getBoolean(K key, boolean defaultValue);
    
    /**
     * 
     */
    public int getInteger(K key);
    
    /**
     * 
     */
    public int getInteger(K key, int defaultValue);
    
    /**
     * 
     */
    public float getFloat(K key);
    
    /**
     * 
     */
    public float getFloat(K key, float defaultValue);
    
    /**
     * 
     */
    public double getDouble(K key);
    
    /**
     * 
     */
    public double getDouble(K key, double defaultValue);
}
