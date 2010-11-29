/*
 * Copyright 2010 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.lang;

import java.util.concurrent.atomic.AtomicBoolean;


public class NumberUtils {

    private NumberUtils() {}
    
    /**
     * Returns true if the given value is undefined in the sense that
     * {@link Double#isNaN(double)} or {@link Double#isInfinite(double)}
     * would return true.
     */
    public static boolean isUndefined(double value) {
        return Double.isNaN(value) || Double.isInfinite(value);
    }
    
    /**
     * Returns true if the given value is undefined in the sense that
     * {@link Float#isNaN(double)} or {@link Float#isInfinite(double)}
     * would return true.
     */
    public static boolean isUndefined(float value) {
        return Float.isNaN(value) || Float.isInfinite(value);
    }
    
    /**
     * 
     */
    public static boolean getBoolean(Object value) {
        return getBoolean(value, false, false);
    }
    
    /**
     * 
     */
    public static boolean getBoolean(Object value, boolean defaultValue) {
        return getBoolean(value, defaultValue, true);
    }
    
    /**
     * 
     */
    private static boolean getBoolean(Object value, 
            boolean defaultValue, boolean hasDefault) {
        
        if (value instanceof Boolean) {
            return ((Boolean)value).booleanValue();
        } else if (value instanceof AtomicBoolean) {
            return ((AtomicBoolean)value).get();
        }
        
        if (hasDefault) {
            return defaultValue;
        }
        
        throw new IllegalArgumentException("value=" + value);
    }
    
    /**
     * 
     */
    public static int getInteger(Object value) {
        return getInteger(value, -1, false);
    }
    
    /**
     * 
     */
    public static int getInteger(Object value, int defaultValue) {
        return getInteger(value, defaultValue, true);
    }
    
    /**
     * 
     */
    private static int getInteger(Object value, 
            int defaultValue, boolean hasDefault) {
        
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        
        if (hasDefault) {
            return defaultValue;
        }
        
        throw new IllegalArgumentException("value=" + value);
    }
    
    /**
     * 
     */
    public static float getFloat(Object value) {
        return getFloat(value, Float.NaN, false);
    }
    
    /**
     * 
     */
    public static float getFloat(Object value, float defaultValue) {
        return getFloat(value, defaultValue, true);
    }
    
    /**
     * 
     */
    private static float getFloat(Object value, 
            float defaultValue, boolean hasDefault) {
        
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        
        if (hasDefault) {
            return defaultValue;
        }
        
        throw new IllegalArgumentException("value=" + value);
    }
    
    /**
     * 
     */
    public static double getDouble(Object value) {
        return getDouble(value, Double.NaN, false);
    }
    
    /**
     * 
     */
    public static double getDouble(Object value, double defaultValue) {
        return getDouble(value, defaultValue, true);
    }
    
    /**
     * 
     */
    private static double getDouble(Object value, 
            double defaultValue, boolean hasDefault) {
        
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        
        if (hasDefault) {
            return defaultValue;
        }
        
        throw new IllegalArgumentException("value=" + value);
    }

    /**
     * Returns the closest power of two value that's greater or 
     * equal to the given argument.
     */
    public static int nextPowOfTwo(int expected) {
        int value = 1;
        while (value < expected) {
            value <<= 1;
        }
        return value;
    }
}
