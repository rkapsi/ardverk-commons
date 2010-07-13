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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A {@link Map} with a fixed-size capacity.
 */
public class FixedSizeHashMap<K, V> extends LinkedHashMap<K, V> 
        implements FixedSize, Serializable {
    
    private static final long serialVersionUID = -8289709441678695668L;
    
    protected final int maxSize;

    public FixedSizeHashMap(int maxSize) {
        super();
        
        if (maxSize < 0 && maxSize != -1) {
            throw new IllegalArgumentException("maxSize=" + maxSize);
        }
        
        this.maxSize = maxSize;
    }

    public FixedSizeHashMap(int initialCapacity, float loadFactor, 
            boolean accessOrder, int maxSize) {
        super(initialCapacity, loadFactor, accessOrder);
        
        if (maxSize < 0 && maxSize != -1) {
            throw new IllegalArgumentException("maxSize=" + maxSize);
        }
        
        this.maxSize = maxSize;
    }

    public FixedSizeHashMap(int initialCapacity, float loadFactor, int maxSize) {
        super(initialCapacity, loadFactor);
        
        if (maxSize < 0 && maxSize != -1) {
            throw new IllegalArgumentException("maxSize=" + maxSize);
        }
        
        this.maxSize = maxSize;
    }

    public FixedSizeHashMap(int initialCapacity, int maxSize) {
        super(initialCapacity);
        
        if (maxSize < 0 && maxSize != -1) {
            throw new IllegalArgumentException("maxSize=" + maxSize);
        }
        
        this.maxSize = maxSize;
    }

    public FixedSizeHashMap(Map<? extends K, ? extends V> m, int maxSize) {
        super(m);
        
        if (maxSize < 0 && maxSize != -1) {
            throw new IllegalArgumentException("maxSize=" + maxSize);
        }
        
        this.maxSize = maxSize;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }
    
    @Override
    public boolean isFull() {
        return maxSize != -1 && size() >= maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (maxSize != -1 && size() > maxSize) {
            removing(eldest);
            return true;
        }
        return false;
    }
    
    /**
     * 
     */
    protected void removing(Map.Entry<K, V> eldest) {
        // OVERRIDE
    }
}
