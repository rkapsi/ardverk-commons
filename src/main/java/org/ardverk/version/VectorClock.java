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

package org.ardverk.version;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VectorClock<K> implements Version<VectorClock<K>>, Cloneable, Serializable {
    
    private static final long serialVersionUID = -1530003204298636637L;
    
    private final Map<K, Value> map = new HashMap<K, Value>();
    
    public VectorClock() {
    }
    
    public VectorClock(K key) {
        map.put(key, new Value(1));
    }
    
    public int add(K key) {
        Value value = map.get(key);
        if (value == null) {
            value = new Value();
            map.put(key, value);
        }
        return value.tick();
    }
    
    public int get(K key) {
        Value value = map.get(key);
        return value != null ? value.get() : 0;
    }
    
    public boolean contains(K key) {
        return map.containsKey(key);
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public int size() {
        return map.size();
    }
    
    private Value lookup(K key) {
        return map.get(key);
    }
    
    private Set<Map.Entry<K, Value>> entrySet() {
        return map.entrySet();
    }
    
    private Set<K> keySet() {
        return map.keySet();
    }
    
    @Override
    public Occured compareTo(VectorClock<K> other) {
        boolean bigger1 = false;
        boolean bigger2 = false;
        
        int size1 = size();
        int size2 = other.size();
        
        if (size1 < size2) {
            bigger2 = true;
            
        } else if (size2 < size1) {
            bigger1 = true;
            
        } else {
            for (Map.Entry<K, Value> entry : entrySet()) {
                Value value = other.lookup(entry.getKey());
                if (value == null) {
                    bigger1 = true;
                    
                    for (K key : other.keySet()) {
                        if (!contains(key)) {
                            bigger2 = true;
                            break;
                        }
                    }
                    
                    break;
                }
                
                int diff = entry.getValue().compareTo(value);
                if (diff < 0) {
                    bigger2 = true;
                    break;
                } else if (0 < diff) {
                    bigger1 = true;
                    break;
                }
            }
        }
        
        if (!bigger1 && !bigger2) {
            // Both VectorClocks are the same.
            return Occured.BEFORE;
        } else if (bigger1 && !bigger2) {
            // This VectorClock represents an event that
            // happened after the other other one.
            return Occured.AFTER;
        } else if (!bigger1 && bigger2) {
            // This VectorClock represents an event that
            // happened before the other other one.
            return Occured.BEFORE;
        }
        
        // Conflict!!! ;-(
        return Occured.CONCURRENTLY;
    }

    public VectorClock<K> merge(VectorClock<? extends K> other) {
        VectorClock<K> dst = clone();
        
        for (Map.Entry<? extends K, Value> entry : other.entrySet()) {
            K key = entry.getKey();
            Value value = entry.getValue();
            
            Value existing = dst.lookup(key);
            if (existing == null) {
                dst.map.put(key, value.clone());
            } else {
                existing.merge(value);
            }
        }
        
        return dst;
    }
    
    @Override
    public VectorClock<K> clone() {
        VectorClock<K> dst = new VectorClock<K>();
        
        for (Map.Entry<K, Value> entry : entrySet()) {
            dst.map.put(entry.getKey(), entry.getValue().clone());
        }
        
        return dst;
    }
    
    @Override
    public String toString() {
        return map.toString();
    }
    
    private static class Value implements Comparable<Value>, Cloneable, Serializable {
        
        private static final long serialVersionUID = 4596735614527205911L;
        
        private int value;
        
        public Value() {
            this(0);
        }
        
        public Value(int value) {
            this.value = value;
        }
        
        public int get() {
            return value;
        }
        
        public void merge(Value other) {
            this.value = Math.max(value, other.value);
        }
        
        public int tick() {
            return ++value;
        }
        
        @Override
        public int compareTo(Value o) {
            return value - o.value;
        }
        
        
        @Override
        public Value clone() {
            return new Value(value);
        }
        
        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
}
