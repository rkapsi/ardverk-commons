/*
 * Copyright 2009-2011 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ardverk.concurrent;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.ardverk.lang.Longs;

public class Duration implements Comparable<Duration>, Serializable {
    
    private static final long serialVersionUID = -7966576555871244178L;

    public static final Duration ZERO = new Duration(0L, TimeUnit.MILLISECONDS);
    
    private final long duration;
    
    private final TimeUnit unit;
    
    public Duration(long duration, TimeUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }
    
    public long getDuration(TimeUnit unit) {
        return unit.convert(duration, this.unit);
    }
    
    public long getDurationInMillis() {
        return getDuration(TimeUnit.MILLISECONDS);
    }
    
    public long getTime() {
        return System.currentTimeMillis() + getDurationInMillis();
    }
    
    public Date getDate() {
        return new Date(getTime());
    }
    
    @Override
    public int compareTo(Duration o) {
        return Longs.compare(getDurationInMillis(), o.getDurationInMillis());
    }

    @Override
    public int hashCode() {
        return (int)getDurationInMillis();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Duration)) {
            return false;
        }
        
        return getDurationInMillis() == ((Duration)o).getDurationInMillis();
    }
    
    @Override
    public String toString() {
        return duration + " " + unit;
    }
}
