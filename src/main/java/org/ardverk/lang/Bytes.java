/*
 * Copyright 2010-2011 Roger Kapsi
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

import java.util.Comparator;

public class Bytes {
    
    /**
     * An empty {@code byte} array.
     */
    public static final byte[] EMPTY = new byte[0];

    private Bytes() {}
    
    /**
     * Compares the given {@code byte} values.
     * 
     * @see Comparable
     * @see Comparator
     */
    public static int compare(byte b1, byte b2) {
        if (b1 < b2) {
            return -1;
        } else if (b2 < b1) {
            return 1;
        }
        return 0;
    }
    
    /**
     * Compares the given {@code byte}s as unsigned values.
     * 
     * @see Comparable
     * @see Comparator
     */
    public static int compareUnsigned(int b1, int b2) {
        return (b1 & 0xFF) - (b2 & 0xFF);
    }
}