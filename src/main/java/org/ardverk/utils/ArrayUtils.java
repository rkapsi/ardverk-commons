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

package org.ardverk.utils;

import java.util.Random;

import org.ardverk.security.SecurityUtils;

public class ArrayUtils {

    private static final Random GENERATOR 
        = SecurityUtils.createSecureRandom();
    
    private ArrayUtils() {}
    
    /**
     * Swaps the array's elements.
     */
    public static <T> void swap(T[] elements, int i, int j) {
        T element = elements[i];
        elements[i] = elements[j];
        elements[j] = element;
    }
    
    /**
     * Shuffles the array's elements.
     */
    public static <T> T[] shuffle(T... elements) {
        return shuffle(elements, 0, elements.length);
    }
    
    /**
     * Shuffles the array's elements.
     */
    public static <T> T[] shuffle(T[] elements, int offset, int length) {
        return shuffle(GENERATOR, elements, offset, length);
    }
    
    /**
     * Shuffles the array's elements with the given {@link Random}.
     */
    public static <T> T[] shuffle(Random random, 
            T[] elements, int offset, int length) {
        for (int i = 0; i < length; i++) {
            swap(elements, offset + i, offset + random.nextInt(length));
        }
        return elements;
    }
}
