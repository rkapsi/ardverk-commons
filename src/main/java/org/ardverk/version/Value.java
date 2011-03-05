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

public class Value implements Comparable<Value>, Serializable {
    
    private static final long serialVersionUID = -1915316363583960219L;

    public static final Value INIT = new Value(0);
    
    private final long creationTime;
    
    private final int value;
    
    public Value(int value) {
        this(System.currentTimeMillis(), value);
    }
    
    public Value(long creationTime, int value) {
        this.creationTime = creationTime;
        this.value = value;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public int get() {
        return value;
    }
    
    public Value increment() {
        return new Value(value + 1);
    }

    public Value max(Value other) {
        if (value < other.value) {
            return other;
        }
        return this;
    }
    
    @Override
    public int compareTo(Value o) {
        return value - o.value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}