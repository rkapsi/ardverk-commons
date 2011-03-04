package org.ardverk.version;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VectorClock<K> implements Version<VectorClock<K>>, Cloneable, Serializable {
    
    private static final long serialVersionUID = -1530003204298636637L;
    
    private final Map<K, Value> map = new HashMap<K, Value>();
    
    public synchronized int increment(K key) {
        Value clock = map.get(key);
        if (clock == null) {
            clock = new Value();
            map.put(key, clock);
        }
        return clock.tick();
    }
    
    public synchronized int get(K key) {
        Value clock = map.get(key);
        return clock != null ? clock.get() : 0;
    }
    
    public synchronized boolean contains(K key) {
        return map.containsKey(key);
    }
    
    private Value entry(K key) {
        return map.get(key);
    }
    
    private Set<Map.Entry<K, Value>> entrySet() {
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
        
        for (Map.Entry<K, Value> entry : entrySet()) {
            K key = entry.getKey();
            Value value = entry.getValue();
            
            Value otherValue = other.entry(key);
            if (otherValue != null) {
                int diff = value.compareTo(otherValue);
                if (diff < 0) {
                    equal = false;
                    after = false;
                } else if (0 < diff) {
                    equal = false;
                    before = false;
                }
            } else {
                equal = false;
                before = false;
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
    public synchronized VectorClock<K> clone() {
        VectorClock<K> dst = new VectorClock<K>();
        for (Map.Entry<K, Value> entry : entrySet()) {
            dst.map.put(entry.getKey(), entry.getValue().clone());
        }
        return dst;
    }
    
    @Override
    public synchronized String toString() {
        return map.toString();
    }
    
    private static class Value implements Comparable<Value>, Cloneable, Serializable {
        
        private static final long serialVersionUID = 4596735614527205911L;
        
        private int value = 0;
        
        public int get() {
            return value;
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
            Value dst = new Value();
            dst.value = value;
            return dst;
        }
        
        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
}
