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

package org.ardverk.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.ardverk.lang.Arguments;

public class CollectionUtils {

    private static enum Position {
        FIRST,
        LAST,
        NTH;
    }

    private CollectionUtils() {
    }

    /**
     * Makes a copy of src and returns it.
     */
    public static <K, V> Map<K, V> copy(Map<? extends K, ? extends V> src) {
        return copyTo(src, null);
    }

    /**
     * Copies src to dst if src is not null and not empty. It will create and
     * return a new Map if dst is either null or {@link Collections#EMPTY_MAP}.
     */
    public static <K, V> Map<K, V> copyTo(Map<? extends K, ? extends V> src,
            Map<K, V> dst) {

        if (src != null && !src.isEmpty()) {
            if (dst == null) {
                dst = new HashMap<K, V>(src);
            } else {
                dst.putAll(src);
            }
        }

        return dst;
    }

    /**
     * Returns the first element of the given object which is assumed to
     * be either an array or an instance of {@link Iterable}.
     */
    @SuppressWarnings("unchecked")
    public static <V> V first(V values) {
        if (values instanceof Object[]) {
            return first((V[])values);
        }
        
        return first((Iterable<V>)values);
    }
    
    /**
     * Returns the last element of the given object which is assumed to
     * be either an array or an instance of {@link Iterable}.
     */
    @SuppressWarnings("unchecked")
    public static <V> V last(V values) {
        if (values instanceof Object[]) {
            return last((V[])values);
        }
        
        return last((Iterable<V>)values);
    }
    
    /**
     * Returns the nth element of the given object which is assumed to
     * be either an array or an instance of {@link Iterable}.
     */
    @SuppressWarnings("unchecked")
    public static <V> V nth(V values, int n) {
        if (values instanceof Object[]) {
            return nth((V[])values, n);
        }
        
        return nth((Iterable<V>)values, n);
    }
    
    /**
     * Returns the first element from the given {@link Iterable}.
     */
    public static <V> V first(Iterable<? extends V> values) {
        return nth(values, Position.FIRST, 0);
    }

    /**
     * Returns the last element from the given {@link Iterable}.
     */
    public static <V> V last(Iterable<? extends V> values) {
        return nth(values, Position.LAST, -1);
    }

    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    public static <V> V nth(Iterable<? extends V> values, int n) {
        return nth(values, Position.NTH, Arguments.notNegative(n, "n"));
    }
    
    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    private static <V> V nth(Iterable<? extends V> values, Position position, int n) {
        
        // We can take shortcuts for some types of Collections.
        if (values instanceof Collection<?>) {
            Collection<? extends V> c = (Collection<? extends V>)values;
            
            if (c instanceof List<?>) {
                if (isFirst(c, position, n)) {
                    return ((List<? extends V>)c).get(0);
                } else if (isLast(c, position, n)) {
                    return ((List<? extends V>)c).get(c.size()-1);
                }
                return ((List<? extends V>)c).get(n);
            } else if (c instanceof SortedSet<?>) {
                if (isFirst(c, position, n)) {
                    return ((SortedSet<? extends V>) c).first();
                } else if (isLast(c, position, n)) {
                    return ((SortedSet<? extends V>) c).last();
                }
            } else if (c instanceof Deque<?>) {
                if (isFirst(c, position, n)) {
                    return ((Deque<? extends V>) c).getFirst();
                } else if (isLast(c, position, n)) {
                    return ((Deque<? extends V>) c).getLast();
                }
            }
        }

        Iterator<? extends V> it = values.iterator();
        if (it.hasNext()) {
            
            int counter = n;
            V value = null;
            
            while (it.hasNext()) {
                value = it.next();
    
                if (counter == 0) {
                    return value;
                }
    
                --counter;
            }
    
            if (position == Position.LAST) {
                return value;
            }
        }

        throw new IndexOutOfBoundsException("position=" + position + ", n=" + n);
    }
    
    /**
     * Returns the first element of the given array.
     */
    public static <V> V first(V[] values) {
        return nth(values, 0);
    }
    
    /**
     * Returns the last element of the given array.
     */
    public static <V> V last(V[] values) {
        return nth(values, values.length-1);
    }
   
    /**
     * Returns the nth element of the given array.
     */
    public static <V> V nth(V[] values, int n) {
        return values[n];
    }
    
    /**
     * Returns true if the first element needs to be retrieved 
     * from the given {@link Collection}.
     */
    private static boolean isFirst(Collection<?> c, Position position, int n) {
        return position == Position.FIRST || n == 0;
    }
    
    /**
     * Returns true if the last element needs to be retrieved 
     * from the given {@link Collection}.
     */
    private static boolean isLast(Collection<?> c, Position position, int n) {
        return position == Position.LAST || n == (c.size()-1);
    }
}
