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

import java.lang.reflect.Array;
import java.util.Comparator;
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
    
    /**
     * Returns the min value in the given array.
     */
    public static byte min(byte... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static byte min(byte[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static byte max(byte... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static byte max(byte[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static byte[] mm(byte... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static byte[] mm(byte[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        byte min = values[offset];
        byte max = min;
        
        int end = offset + length;
        while (++offset < end) {
            byte value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new byte[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static short min(short... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static short min(short[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static short max(short... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static short max(short[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static short[] mm(short... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static short[] mm(short[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        short min = values[offset];
        short max = min;
        
        int end = offset + length;
        while (++offset < end) {
            short value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new short[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static char min(char... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static char min(char[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static char max(char... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static char max(char[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static char[] mm(char... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static char[] mm(char[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        char min = values[offset];
        char max = min;
        
        int end = offset + length;
        while (++offset < end) {
            char value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new char[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static int min(int... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static int min(int[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static int max(int... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static int max(int[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static int[] mm(int... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static int[] mm(int[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        int min = values[offset];
        int max = min;
        
        int end = offset + length;
        while (++offset < end) {
            int value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new int[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static float min(float... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static float min(float[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static float max(float... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static float max(float[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static float[] mm(float... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static float[] mm(float[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        float min = values[offset];
        float max = min;
        
        int end = offset + length;
        while (++offset < end) {
            float value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new float[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static long min(long... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static long min(long[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static long max(long... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static long max(long[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static long[] mm(long... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static long[] mm(long[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        long min = values[offset];
        long max = min;
        
        int end = offset + length;
        while (++offset < end) {
            long value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new long[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static double min(double... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static double min(double[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static double max(double... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static double max(double[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static double[] mm(double... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static double[] mm(double[] values, int offset, int length) {
        checkBounds(values, offset, length);
        
        double min = values[offset];
        double max = min;
        
        int end = offset + length;
        while (++offset < end) {
            double value = values[offset];
            if (value < min) {
                min = value;
            } else if (max < value) {
                max = value;
            }
        }
        
        return new double[] { min, max };
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static <T extends Number & Comparable<T>> T min(T... values) {
        return min(values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static <T extends Number & Comparable<T>> T min(T[] values, int offset, int length) {
        return mm(values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static <T extends Number & Comparable<T>> T max(T... values) {
        return max(values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static <T extends Number & Comparable<T>> T max(T[] values, int offset, int length) {
        return mm(values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static <T extends Number & Comparable<T>> T[] mm(T... values) {
        return mm(values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static <T extends Number & Comparable<T>> T[] mm(T[] values, int offset, int length) {
        Comparator<T> comparator = DefaultComparator.create();
        return mm(comparator, values, offset, length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static <T> T min(Comparator<T> comparator, T... values) {
        return min(comparator, values, 0, values.length);
    }
    
    /**
     * Returns the min value in the given array.
     */
    public static <T> T min(Comparator<T> comparator, T[] values, int offset, int length) {
        return mm(comparator, values, 0, values.length)[0];
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static <T> T max(Comparator<T> comparator, T... values) {
        return max(comparator, values, 0, values.length);
    }
    
    /**
     * Returns the max value in the given array.
     */
    public static <T> T max(Comparator<T> comparator, T[] values, int offset, int length) {
        return mm(comparator, values, 0, values.length)[1];
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static <T> T[] mm(Comparator<T> comparator, T... values) {
        return mm(comparator, values, 0, values.length);
    }
    
    /**
     * Returns the min and max value in the given array.
     */
    public static <T> T[] mm(Comparator<T> comparator, 
            T[] values, int offset, int length) {
        
        checkBounds(values, offset, length);
        
        T min = values[offset];
        T max = min;
        
        int end = offset + length;
        while (++offset < end) {
            T value = values[offset];
            
            if (comparator.compare(value, min) < 0) {
                min = value;
            } else if (0 < comparator.compare(value, max)) {
                max = value;
            }
        }
        
        Class<?> clazz = values.getClass();
        Class<?> componentType = clazz.getComponentType();
        @SuppressWarnings("unchecked")
        T[] dst = (T[]) Array.newInstance(componentType, 2);
        dst[0] = min;
        dst[1] = max;
        return dst;
    }
    
    /**
     * Returns {@code true} if the given element is in the array.
     */
    public static boolean contains(Object element, Object[] elements) {
        return contains(element, elements, 0, elements.length);
    }
    
    /**
     * Returns {@code true} if the given element is in the array.
     */
    public static boolean contains(Object element, Object[] elements, 
            int offset, int length) {
        return indexOf(element, elements, offset, length) != -1;
    }
    
    /**
     * Returns the element's index in the given array or -1
     * if the array doesn't contain the given element.
     */
    public static int indexOf(Object element, Object[] elements) {
        return indexOf(element, elements, 0, elements.length);
    }
    
    /**
     * Returns the element's index in the given array or -1
     * if the array doesn't contain the given element.
     */
    public static int indexOf(Object element, Object[] elements, 
            int offset, int length) {
        
        checkBounds(elements, offset, length);
        
        for (int i = 0; i < length; i++) {
            int index = offset + i;
            Object other = elements[index];
            if (element == other || element.equals(other)) {
                return index;
            }
        }
        return -1;
    }
    
    /**
     * Checks if the array's bounds are correct.
     */
    private static void checkBounds(Object values, int offset, int length) {
        if (offset < 0 || length <= 0 || Array.getLength(values) < (offset+length)) {
            throw new IllegalArgumentException(
                    "offset=" + offset + ", length=" + length);
        }
    }
}
