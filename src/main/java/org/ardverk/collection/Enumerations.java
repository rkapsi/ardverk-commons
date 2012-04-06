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

package org.ardverk.collection;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An utility class to make conversion between {@link Iterable}, 
 * {@link Iterator} and {@link Enumeration} relatively easy.
 */
public class Enumerations {

  private Enumerations() {}
  
  /**
   * An {@link Enumeration} that has no elements.
   */
  private static final Enumeration<Object> EMPTY = new Enumeration<Object>() {
    @Override
    public Object nextElement() {
      throw new NoSuchElementException();
    }
    
    @Override
    public boolean hasMoreElements() {
      return false;
    }
  };
  
  /**
   * Returns an empty {@link Enumeration}.
   */
  @SuppressWarnings("unchecked")
  public static <T> Enumeration<T> empty() {
    return (Enumeration<T>)EMPTY;
  }
  
  /**
   * Creates and returns an {@link Iterator} for the given {@link Enumeration}.
   */
  public static <T> Iterator<T> toIterator(final Enumeration<T> e) {
    if (!e.hasMoreElements()) {
      return Iterators.empty();
    }
    
    return new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return e.hasMoreElements();
      }

      @Override
      public T next() {
        return e.nextElement();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
  
  /**
   * Creates and returns an {@link Iterable} for the given {@link Enumeration}.
   */
  public static <T> Iterable<T> toIterable(final Enumeration<T> e) {
    return Iterables.fromIterator(toIterator(e));
  }
  
  /**
   * Creates and returns an {@link Enumeration} for the given {@link Iterator}.
   */
  public static <T> Enumeration<T> fromIterator(final Iterator<T> it) {
    if (!it.hasNext()) {
      return empty();
    }
    
    return new Enumeration<T>() {
      @Override
      public boolean hasMoreElements() {
        return it.hasNext();
      }

      @Override
      public T nextElement() {
        return it.next();
      }
    };
  }
  
  /**
   * Creates and returns an {@link Enumeration} for the given {@link Iterable}.
   */
  public static <T> Enumeration<T> fromIterable(Iterable<T> it) {
    return fromIterator(it.iterator());
  }
}