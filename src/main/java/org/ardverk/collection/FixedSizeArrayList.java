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

import java.util.ArrayList;
import java.util.Collection;

public class FixedSizeArrayList<E> extends ArrayList<E> implements FixedSize {

  private static final long serialVersionUID = -8835179064931233224L;
  
  private final int maxSize;

  public FixedSizeArrayList(int maxSize) {
    super();
    
    if (maxSize < 0 && maxSize != -1) {
      throw new IllegalArgumentException("maxSize=" + maxSize);
    }
    
    this.maxSize = maxSize;
  }

  public FixedSizeArrayList(Collection<? extends E> c, int maxSize) {
    super(c);
    
    if (maxSize < 0 && maxSize != -1) {
      throw new IllegalArgumentException("maxSize=" + maxSize);
    }
    
    this.maxSize = maxSize;
  }

  public FixedSizeArrayList(int initialCapacity, int maxSize) {
    super(initialCapacity);
    
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
  public boolean add(E e) {
    if (super.add(e)) {
      adjustSize(size()-1, e);
      return true;
    }
    
    return false;
  }

  @Override
  public void add(int index, E element) {
    if (index < 0 || size() < index) {
      throw new IllegalArgumentException("index=" + index);
    }
    
    super.add(index, element);
    adjustSize(index, element);
  }
  
  private void adjustSize(int index, E e) {
    if (maxSize != -1 && size() > maxSize) {
      E old = remove(eject(index, e));
      removed(old);
    }
  }
  
  protected int eject(int index, E e) {
    return 0;
  }
  
  protected void removed(E element) {
    // OVERRIDE
  }
}