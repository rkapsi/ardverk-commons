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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A default implementation of {@link OrderedMap}.
 */
public class OrderedHashMap<K, V> extends LinkedHashMap<K, V> 
        implements OrderedMap<K, V> {
    
    private static final long serialVersionUID = 1555501814618753582L;

    public OrderedHashMap() {
        super();
    }

    public OrderedHashMap(int initialCapacity, float loadFactor,
            boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public OrderedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public OrderedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public OrderedHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public java.util.Map.Entry<K, V> firstEntry() {
        return CollectionUtils.first(entrySet());
    }

    @Override
    public java.util.Map.Entry<K, V> lastEntry() {
        return CollectionUtils.last(entrySet());
    }

    @Override
    public java.util.Map.Entry<K, V> nthEntry(int index) {
        return CollectionUtils.nth(entrySet(), index);
    }

    @Override
    public K firstKey() {
        return firstEntry().getKey();
    }

    @Override
    public K lastKey() {
        return lastEntry().getKey();
    }

    @Override
    public K nthKey(int index) {
        return nthEntry(index).getKey();
    }

    @Override
    public V firstValue() {
        return firstEntry().getValue();
    }

    @Override
    public V lastValue() {
        return lastEntry().getValue();
    }

    @Override
    public V nthValue(int index) {
        return nthEntry(index).getValue();
    }
}
