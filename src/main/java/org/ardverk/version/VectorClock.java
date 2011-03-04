package org.ardverk.version;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VectorClock<K> implements Version<VectorClock<K>>, Cloneable, Serializable {
    
    private static final long serialVersionUID = -1530003204298636637L;
    
    private final Map<K, Clock> map = new HashMap<K, Clock>();
    
    public synchronized int increment(K key) {
        Clock clock = map.get(key);
        if (clock == null) {
            clock = new Clock();
            map.put(key, clock);
        }
        return clock.tick();
    }
    
    public synchronized int get(K key) {
        Clock clock = map.get(key);
        return clock != null ? clock.get() : 0;
    }
    
    public synchronized boolean contains(K key) {
        return map.containsKey(key);
    }
    
    private Clock entry(K key) {
        return map.get(key);
    }
    
    private Set<Map.Entry<K, Clock>> entrySet() {
        return map.entrySet();
    }
    
    private Set<K> keySet() {
        return map.keySet();
    }
    
    @Override
    public synchronized Occured compareTo(VectorClock<K> other) {
        
        boolean equal = true;
        boolean before = true;
        boolean after = true;
        
        for (Map.Entry<K, Clock> entry : entrySet()) {
            K key = entry.getKey();
            Clock value = entry.getValue();
            
            Clock otherValue = other.entry(key);
            if (otherValue != null) {
                
            }
        }
        
        for (K key : other.keySet()) {
            if (!contains(key)) {
                equal = false;
                after = false;
                break;
            }
        }

        if (equal) {
            return Occured.EQUAL;
        } else if (before && !after) {
            return Occured.BEFORE;
        } else if (!before && after) {
            return Occured.AFTER;
        }
        
        return Occured.CONCURRENTLY;
    }

    @Override
    public VectorClock<K> clone() {
        return null;
    }
    
    private static class Clock implements Cloneable, Serializable {
        
        private static final long serialVersionUID = 4596735614527205911L;
        
        private int value = 0;
        
        public int get() {
            return value;
        }
        
        public int tick() {
            return ++value;
        }
        
        @Override
        public Clock clone() {
            Clock clock = new Clock();
            clock.value = value;
            return clock;
        }
    }
}
