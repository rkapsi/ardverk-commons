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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * An implementation of a Vector Clock. This class is immutable!
 * 
 * @see http://en.wikipedia.org/wiki/Vector_clock
 */
public class VectorClock<K> implements Version<VectorClock<K>>, Serializable {
    
    private static final long serialVersionUID = 8061383748163285648L;
    
    public static <K> VectorClock<K> create() {
        return new VectorClock<K>(System.currentTimeMillis(), 
                Collections.<K, Vector>emptyMap());
    }
    
    public static <K> VectorClock<K> create(K key) {
        VectorClock<K> clock = create();
        return clock.append(key);
    }
    
    public static <K> VectorClock<K> create(long creationTime, 
            Map<? extends K, ? extends Vector> map) {
        return new VectorClock<K>(creationTime, map);
    }
    
    private final long creationTime;
    
    private final Map<? extends K, ? extends Vector> map;
    
    private volatile int hashCode = 0;
    
    private VectorClock(long creationTime, 
            Map<? extends K, ? extends Vector> map) {
        this.creationTime = creationTime;
        this.map = map;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public VectorClock<K> append(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key=null");
        }
        
        Map<K, Vector> dst = new TreeMap<K, Vector>(map);
        
        Vector vector = dst.get(key);
        if (vector == null) {
            vector = Vector.INIT;
        }
        
        dst.put(key, vector.increment());
        return new VectorClock<K>(creationTime, dst);
    }
    
    public boolean contains(K key) {
        return map.containsKey(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public Vector get(K key) {
        return map.get(key);
    }
    
    public Set<? extends Map.Entry<? extends K, ? extends Vector>> entrySet() {
        return Collections.unmodifiableSet(map.entrySet());
    }
    
    public Set<? extends K> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }
    
    public Collection<? extends Vector> values() {
        return Collections.unmodifiableCollection(map.values());
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
            for (Map.Entry<? extends K, ? extends Vector> entry : entrySet()) {
                Vector vector = other.get(entry.getKey());
                if (vector == null) {
                    bigger1 = true;
                    
                    for (K key : other.keySet()) {
                        if (!contains(key)) {
                            bigger2 = true;
                            break;
                        }
                    }
                    
                    break;
                }
                
                int diff = entry.getValue().compareTo(vector);
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
            return Occured.IDENTICAL;
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
        Map<K, Vector> dst = new TreeMap<K, Vector>(map);
        
        for (Map.Entry<? extends K, ? extends Vector> entry : other.entrySet()) {
            K key = entry.getKey();
            Vector vector = entry.getValue();
            
            Vector existing = dst.get(key);
            if (existing != null) {
                vector = existing.max(vector);
            }
            
            dst.put(key, vector);
        }
        
        long creationTime = Math.min(getCreationTime(), other.getCreationTime());
        return new VectorClock<K>(creationTime, dst);
    }
    
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            int value = 0;
            for (Map.Entry<?, ?> entry : entrySet()) {
                value = 31 * value + entry.getKey().hashCode();
                value = 31 * value + entry.getValue().hashCode();
            }
            hashCode = value;
        }
        
        return hashCode;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof VectorClock<?>)) {
            return false;
        }
        
        VectorClock<?> clock = (VectorClock<?>)o;
        if (clock.size() != size()) {
            return false;
        }
        
        Iterator<? extends Map.Entry<?, ?>> it 
                    = clock.entrySet().iterator();
        
        for (Map.Entry<?, ?> entry : entrySet()) {
            Map.Entry<?, ?> other = it.next();
            
            if (!entry.getKey().equals(other.getKey()) 
                    || !entry.getValue().equals(other.getValue())) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return creationTime + ", " + map.toString();
    }
}
