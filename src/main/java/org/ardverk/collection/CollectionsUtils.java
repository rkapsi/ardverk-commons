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

public class CollectionsUtils {

    private static enum Element {
	FIRST {
	    public int convert(Iterable<?> src, int index) {
		return 0;
	    }
	},
	
	LAST {
	    public int convert(Iterable<?> src, int index) {
		if (src instanceof Collection<?>) {
		    return ((Collection<?>)src).size()-1;
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
    
    private CollectionsUtils() {}
    
    /**
     * Makes a copy of src and returns it.
     */
    public static <K, V> Map<K, V> copy(Map<? extends K, ? extends V> src) {
        return copyTo(src, Collections.<K, V>emptyMap());
    }
    
    /**
     * Copies src to dst if src is not null and not empty. It 
     * will create and return a new Map if dst is either null 
     * or {@link Collections#EMPTY_MAP}.
     */
    public static <K, V> Map<K, V> copyTo(
            Map<? extends K, ? extends V> src, Map<K, V> dst) {
        
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
     * Returns the first element from the given {@link Iterable}.
     */
    public static <V> V first(Iterable<? extends V> c) {
        return nth(c, Element.FIRST, 0);
    }
    
    /**
     * Returns the last element from the given {@link Iterable}.
     */
    public static <V> V last(Iterable<? extends V> c) {
        return nth(c, Element.LAST, -1);
    }
    
    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    public static <V> V nth(Iterable<? extends V> c, int n) {
	return nth(c, Element.NTH, Arguments.notNegative(n, "n"));
    }
    
    /**
     * Returns the <tt>nth</tt> element from the given {@link Iterable}.
     */
    private static <V> V nth(Iterable<? extends V> c, Element element, int n) {
	if (c instanceof List<?>) {
            return ((List<? extends V>)c).get(element.convert(c, n));
        } else if (c instanceof SortedSet<?>) {
            if (element == Element.FIRST) {
                return ((SortedSet<? extends V>)c).first();
            } else if (element == Element.LAST) {
                return ((SortedSet<? extends V>)c).last();
            }
        } else if (c instanceof Deque<?>) {
            if (element == Element.FIRST) {
    	    	return ((Deque<? extends V>)c).getFirst();
            } else if (element == Element.LAST) {
                return ((Deque<? extends V>)c).getLast();
            }
        }
        
	Iterator<? extends V> it = c.iterator();
	
	V value = null;
	while (it.hasNext()) {
	    value = it.next();
	    
	    if (n == 0) {
        	return value;
            }
	    
            --n;
	}
	
	if (element == Element.LAST) {
	    return value;
	}
        
        throw new IndexOutOfBoundsException("n=" + n);
    }
}
