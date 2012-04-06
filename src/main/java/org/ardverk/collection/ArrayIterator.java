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

package org.ardverk.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} for arrays.
 */
public class ArrayIterator<T> implements Iterator<T> {

  private static final Object SKIP = new Object();
  
  private final T first;
  
  private final T[] others;
  
  private final int offset;
  
  private final int length;
  
  private int index = -1;
  
  public ArrayIterator(T element) {
    this(element, null, 0, 0);
  }

  public ArrayIterator(T... values) {
    this(values, 0, values.length);
  }
  
  @SuppressWarnings("unchecked")
  public ArrayIterator(T[] values, int offset, int length) {
    this((T)SKIP, values, offset, length);
  }
  
  public ArrayIterator(T first, T... others) {
    this(first, others, 0, others.length);
  }
  
  public ArrayIterator(T first, T[] others, int offset, int length) {
    
    if (others == null) {
      if (first == SKIP) {
        throw new NullPointerException();
      }
    } else {
      if (offset < 0 || length < 0 
          || others.length < (offset+length)) {
        throw new ArrayIndexOutOfBoundsException(
            "offset=" + offset + ", length=" + length);
      }
    }
    
    this.first = first;
    this.others = others;
    this.offset = offset;
    this.length = length;
    
    if (first == SKIP) {
      ++index;
    }
  }

  @Override
  public boolean hasNext() {
    return index < length;
  }

  @Override
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    
    if (index == -1) {
      ++index;
      return first;
    }
    
    return others[offset + (index++)];
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}