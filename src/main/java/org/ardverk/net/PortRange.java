/*
 * Copyright 2010-2012 Roger Kapsi
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

package org.ardverk.net;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An utility class that defines a range of Port-Numbers.
 */
public class PortRange implements Comparable<PortRange>, Serializable, 
    Cloneable, Iterable<Integer> {
  
  private static final long serialVersionUID = 3985791699824812205L;

  /**
   * The minimum port.
   */
  public static final int MIN_PORT = 0x00;
  
  /**
   * The maximum port
   */
  public static final int MAX_PORT = 0xFFFF;
  
  /**
   * The full port range from {@link #MIN_PORT} to {@link #MAX_PORT}
   */
  public static final PortRange FULL 
    = PortRange.valueOf(MIN_PORT, MAX_PORT);
  
  /**
   * The range of port numbers that are usable (i.e. it's 
   * possible to bind a socket to it).
   */
  public static final PortRange USABLE 
    = PortRange.valueOf(0x01, MAX_PORT);
  
  /**
   * The range of usable port numbers that don't require root privileges.
   */
  public static final PortRange NON_ROOT 
    = PortRange.valueOf(1024, MAX_PORT);
  
  /**
   * The range of well-known port numbers
   */
  public static final PortRange WELL_KNOWN 
    = PortRange.valueOf(MIN_PORT, 1023);
  
  /**
   * The range of registered port numbers
   */
  public static final PortRange REGISTERED 
    = PortRange.valueOf(1024, 49151);
  
  /**
   * The range of dynamic port numbers
   */
  public static final PortRange DYNAMIC 
    = PortRange.valueOf(49152, MAX_PORT);
  
  /**
   * Creates and returns a {@link PortRange}
   */
  public static PortRange valueOf(int port) {
    return valueOf(port, port);
  }
  
  /**
   * Creates and returns a {@link PortRange}
   */
  public static PortRange valueOf(int min, int max) {
    return new PortRange(min, max);
  }
  
  private final int min;
  
  private final int max;
  
  private PortRange(int min, int max) {
    
    if (min < MIN_PORT) {
      throw new IllegalArgumentException("min=" + min);
    }
    
    if (MAX_PORT < max) {
      throw new IllegalArgumentException("max=" + max);
    }
    
    if (max < min) {
      throw new IllegalArgumentException(
          "min=" + min + ", max=" + max);
    }
    
    this.min = min;
    this.max = max;
  }
  
  /**
   * Returns the minimum
   */
  public int getMin() {
    return min;
  }
  
  /**
   * Returns the maximum
   */
  public int getMax() {
    return max;
  }
  
  /**
   * Returns a random port between min and max.
   */
  public int getRandom() {
    return min + (int)((max - min) * Math.random());
  }
  
  /**
   * Returns all ports in the min-max range.
   */
  @Override
  public Iterator<Integer> iterator() {
    return iterator(false);
  }
  
  /**
   * Returns an {@link Iterator} that may or may not return ports 
   * in the min-max range.
   */
  private Iterator<Integer> iterator(final boolean random) {
    return new Iterator<Integer>() {

      private int port = min;
      
      @Override
      public boolean hasNext() {
        return port <= max;
      }

      @Override
      public Integer next() {
        if (max < port) {
          throw new NoSuchElementException();
        }
        
        if (random) {
          port = port + (int)((max - port) * Math.random());
        }
        
        return port++;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * Returns {@code true} if the given value is in range
   */
  public boolean contains(int port) {
    return contains(port, port);
  }
  
  /**
   * Returns {@code true} if the given value is in range
   */
  public boolean contains(int min, int max) {
    return this.min <= min && max <= this.max;
  }
  
  /**
   * Returns {@code true} if this contains the given {@link PortRange}.
   */
  public boolean contains(PortRange other) {
    return contains(other.min, other.max);
  }
  
  @Override
  public int hashCode() {
    return 31*min + max;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof PortRange)) {
      return false;
    }
    
    PortRange other = (PortRange)o;
    return min == other.min && max == other.max;
  }

  @Override
  public int compareTo(PortRange o) {
    return (min - o.min) + (max - o.max);
  }
  
  @Override
  public PortRange clone() {
    return this;
  }

  @Override
  public String toString() {
    return min + ", " + max;
  }
  
  /**
   * Returns {@code true} if the given port is valid
   */
  public static boolean isValid(int port) {
    return FULL.contains(port);
  }
  
  /**
   * Returns {@code true} if the given port is in the usable range
   */
  public static boolean isUsable(int port) {
    return USABLE.contains(port);
  }
  
  /**
   * Returns {@code true} if the given port is a well-known port
   */
  public static boolean isWellKnown(int port) {
    return WELL_KNOWN.contains(port);
  }
  
  /**
   * Returns {@code true} if the given port is dynamic port
   */
  public static boolean isDynamic(int port) {
    return DYNAMIC.contains(port);
  }
  
  /*public static void main(String[] args) {
    PortRange range = PortRange.valueOf(1024, 15000);
    Iterator<Integer> it = range.iterator(true);
    while (it.hasNext()) {
      System.out.println(it.next());
    }
  }*/
}