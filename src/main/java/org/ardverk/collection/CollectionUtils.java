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

    private static enum Element {
        FIRST {
            public int convert(Iterable<?> src, int index) {
                return 0;
            }
        },

        LAST {
            public int convert(Iterable<?> src, int index) {
                if (src instanceof Collection<?>) {
                    return ((Collection<?>) src).size() - 1;
                } else {
                    return -1;
                }
            }
        },

        NTH {
            public int convert(Iterable<?> src, int index) {
                return index;
            }
        };

        public abstract int convert(Iterable<?> src, int index);
    }

    private CollectionUtils() {
    }

    /**
     * Makes a copy of src and returns it.
     */
    public static <K, V> Map<K, V> copy(Map<? extends K, ? extends V> src) {
        return copyTo(src, Collections.<K, V> emptyMap());
    }

    /**
     * Copies src to dst if src is not null and not empty. It will create and
     * return a new Map if dst is either null or {@link Collections#EMPTY_MAP}.
     */
    public static <K, V> Map<K, V> copyTo(Map<? extends K, ? extends V> src,
            Map<K, V> dst) {

        if (src != null && !src.isEmpty()) {
            if (dst == null || dst.equals(Collections.EMPTY_MAP)) {
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
        return nth(values, Element.FIRST, 0);
    }

    /**
     * Returns the last element from the given {@link Iterable}.
     */
    public static <V> V last(Iterable<? extends V> values) {
        return nth(values, Element.LAST, -1);
    }

    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    public static <V> V nth(Iterable<? extends V> values, int n) {
        return nth(values, Element.NTH, Arguments.notNegative(n, "n"));
    }
    
    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    private static <V> V nth(Iterable<? extends V> values, Element element, int n) {
        if (values instanceof List<?>) {
            return ((List<? extends V>) values).get(element.convert(values, n));
        } else if (values instanceof SortedSet<?>) {
            if (element == Element.FIRST) {
                return ((SortedSet<? extends V>) values).first();
            } else if (element == Element.LAST) {
                return ((SortedSet<? extends V>) values).last();
            }
        } else if (values instanceof Deque<?>) {
            if (element == Element.FIRST) {
                return ((Deque<? extends V>) values).getFirst();
            } else if (element == Element.LAST) {
                return ((Deque<? extends V>) values).getLast();
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
    
            if (element == Element.LAST) {
                return value;
            }
        }

        throw new IndexOutOfBoundsException("element=" + element + ", n=" + n);
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
}
