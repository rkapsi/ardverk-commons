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

package org.ardverk.collection;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

public class CollectionUtils {

    private static enum Position {
        FIRST,
        LAST,
        NTH;
    }
    
    private CollectionUtils() {
    }

    /**
     * Concatenates the {@link Collection} with the given value(s).
     */
    public static <V, T extends Collection<V>> T concat(T dst, V... values) {
        return concat(dst, Iterables.fromArray(values));
    }
    
    /**
     * Concatenates the {@link Collection} with the given value(s).
     */
    public static <V, T extends Collection<V>> T concat(T dst, 
            V[] values, int index, int length) {
        return concat(dst, Iterables.fromArray(values, index, length));
    }
    
    /**
     * Concatenates the {@link Collection} with the given {@link Iterable}.
     */
    public static <V, T extends Collection<V>> T concat(T dst, 
            Iterable<? extends V> src) {
        for (V value : src) {
            dst.add(value);
        }
        return dst;
    }
    
    /**
     * Creates and returns a {@link HashSet} for the given value(s).
     */
    public static <T> HashSet<T> asHashSet(T... values) {
        return concat(new HashSet<T>(), values);
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
        return nth(values, Position.NTH, n);
    }
    
    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    private static <V> V nth(Iterable<? extends V> values, Position position, int n) {
        
        // Fail fast for negative N values.
        if (position != Position.LAST && n < 0) {
            throw new IndexOutOfBoundsException("n=" + n);
        }
        
        // We can take shortcuts for some types of Collections
        if (values instanceof Collection<?>) {
            Collection<? extends V> c = (Collection<? extends V>)values;
            
            // Fail fast for out of bounds N values
            if (n >= c.size()) {
                throw new IndexOutOfBoundsException("n=" + n);
            }
            
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

        // For everything else we've to use an Iterator which will take O(n) time 
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
     * Returns {@code true} if the first element needs to be retrieved 
     * from the given {@link Collection}.
     */
    private static boolean isFirst(Collection<?> c, Position position, int n) {
        return position == Position.FIRST || n == 0;
    }
    
    /**
     * Returns {@code true} if the last element needs to be retrieved 
     * from the given {@link Collection}.
     */
    private static boolean isLast(Collection<?> c, Position position, int n) {
        return position == Position.LAST || n == (c.size()-1);
    }
    
    /**
     * Turns the given {@link Collection} into an array.
     */
    public static <T> T[] toArray(Collection<? extends T> c, Class<? extends T> componentType) {
        T[] dst = org.ardverk.utils.ArrayUtils.newInstance(componentType, c.size());
        return c.toArray(dst);
    }
}