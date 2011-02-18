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

package org.ardverk.concurrent;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link CountDown}.
 */
public class CountDown implements Serializable {
    
    private static final long serialVersionUID = 2884657259566757243L;

    private final int value;
    
    private final AtomicInteger countdown;
    
    public CountDown(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value=" + value);
        }
        
        this.value = value;
        this.countdown = new AtomicInteger(value);
    }
    
    /**
     * Returns the {@link CountDown}'s initial value.
     */
    public int getInitialValue() {
        return value;
    }
    
    /**
     * Returns the {@link CountDown}'s current value.
     */
    public int getCurrentValue() {
        return countdown.get();
    }
    
    /**
     * Decrements the {@link CountDown} by 1 and returns {@code true}
     * if the {@link CountDown} has reached zero or less.
     */
    public boolean countDown() {
        return countdown.decrementAndGet() <= 0;
    }
}
