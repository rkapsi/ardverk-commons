/*
 * Copyright 2010-2011 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.version;

import java.io.Serializable;

/**
 * A {@link Vector} is an entry in a {@link VectorClock}. This class is immutable.
 */
public final class Vector implements Comparable<Vector>, Serializable {
  
  private static final long serialVersionUID = -1915316363583960219L;

  public static final Vector INIT = new Vector(0);
  
  private final long timeStamp;
  
  private final int value;
  
  public Vector(int value) {
    this(System.currentTimeMillis(), value);
  }
  
  public Vector(long timeStamp, int value) {
    this.timeStamp = timeStamp;
    this.value = value;
  }
  
  /**
   * Returns the {@link Vector}'s time stamp.
   */
  public long getTimeStamp() {
    return timeStamp;
  }
  
  /**
   * Returns the {@link Vector}'s value
   */
  public int getValue() {
    return value;
  }
  
  /**
   * Returns {@code true} if the {@link Vector} is empty.
   */
  public boolean isEmpty() {
    return value == 0;
  }
  
  /**
   * Increments the {@link Vector} by 1 and returns it.
   */
  public Vector increment() {
    return new Vector(value + 1);
  }

  /**
   * Returns the bigger of the two {@link Vector}s.
   */
  public Vector max(Vector other) {
    if (value < other.value) {
      return other;
    }
    return this;
  }
  
  @Override
  public int compareTo(Vector o) {
    return value - o.value;
  }
  
  @Override
  public int hashCode() {
    return value;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof Vector)) {
      return false;
    }
    
    return compareTo((Vector)o) == 0;
  }
  
  @Override
  public String toString() {
    return timeStamp + ":" + Integer.toString(value);
  }
}