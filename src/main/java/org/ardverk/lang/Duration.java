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

package org.ardverk.lang;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * {@link Duration} is an immutable class that holds a duration (a long value)
 * and the {@link TimeUnit} of the {@link Duration}.
 */
public class Duration implements Serializable, Cloneable, Comparable<Duration> {
    
    private static final long serialVersionUID = 8113035746133232743L;

    public static final Duration NONE = new Duration(-1L, TimeUnit.MILLISECONDS);
    
    private final long duration;
    
    private final TimeUnit unit;
    
    /**
     * Creates and returns a {@link Duration} object for the current time.
     */
    public static Duration currentDuration() {
        return new Duration(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    public Duration(long duration, TimeUnit unit) {
        if (duration < 0L && duration != -1L) {
            throw new IllegalArgumentException("time=" + duration);
        }
        
        if (unit == null) {
            throw new NullArgumentException("unit");
        }
        
        this.duration = duration;
        this.unit = unit;
    }
    
    /**
     * Returns the time.
     */
    public long getDuration() {
        return duration;
    }
    
    /**
     * Returns the {@link TimeUnit}.
     */
    public TimeUnit getUnit() {
        return unit;
    }
    
    /**
     * Converts the time to the given {@link TimeUnit} and returns it.
     */
    public long getDuration(TimeUnit unit) {
        return unit.convert(duration, this.unit);
    }
    
    /**
     * Returns the time in milliseconds.
     */
    public long getDurationInMills() {
        return getDuration(TimeUnit.MILLISECONDS);
    }
    
    /**
     * Converts the time to the given {@link TimeUnit} and 
     * returns a new {@link Duration} instance.
     */
    public Duration convert(TimeUnit unit) {
        return this.unit != unit ? new Duration(getDuration(unit), unit) : this;
    }
    
    @Override
    public int hashCode() {
        return (int)unit.toMillis(duration);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Duration)) {
            return false;
        }
        
        return compareTo((Duration)o) == 0;
    }

    @Override
    public int compareTo(Duration other) {
        if (other == null) {
            throw new NullArgumentException("other");
        }
        
        long diff = duration - unit.convert(other.duration, other.unit);
        
        if (diff < 0L) {
            return -1;
        } else if (diff > 0L) {
            return 1;
        }
        
        return 0;
    }
    
    @Override
    public Duration clone() {
        return this;
    }
    
    @Override
    public String toString() {
        return duration + " " + unit;
    }
}
