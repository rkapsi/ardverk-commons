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

/**
 * An utility class to check arguments.
 */
public class Arguments {

    private Arguments() {}
    
    /**
     * Makes sure the given argument is not {@code null}.
     */
    public static <T> T notNull(T t, String message) {
        if (t == null) {
            throw new NullArgumentException(message);
        }
        
        return t;
    }
    
    /**
     * Makes sure the given argument is not negative.
     */
    public static int notNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message + "=" + value);
        }
        
        return value;
    }
    
    /**
     * Makes sure the given argument is not negative.
     */
    public static int greaterZero(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message + "=" + value);
        }
        
        return value;
    }
    
    /**
     * Makes sure the given argument is not negative.
     */
    public static long notNegative(long value, String message) {
        if (value < 0L) {
            throw new IllegalArgumentException(message + "=" + value);
        }
        
        return value;
    }
}
