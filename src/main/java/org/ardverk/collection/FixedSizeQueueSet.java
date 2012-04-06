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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * A {@link FixedSize} {@link QueueSet}.
 */
public class FixedSizeQueueSet<E> extends QueueSet<E> implements FixedSize {

  private static final long serialVersionUID = -6583693304395649651L;
  
  private final int maxSize;
  
  /**
   * Creates an empty {@link FixedSizeQueueSet}.
   */
  public FixedSizeQueueSet() {
    this(-1);
  }
  
  /**
   * Creates a {@link FixedSizeQueueSet} with the given max size.
   */
  public FixedSizeQueueSet(int maxSize) {
    this(new LinkedList<E>(), new HashSet<E>(), maxSize);
  }

  /**
   * Creates a {@link FixedSizeQueueSet} with the given {@link Queue} 
   * and {@link Set}.
   * 
   * <p>NOTE: The {@link FixedSizeQueueSet} does not check if the
   * initial state of the given arguments is correct.
   */
  public FixedSizeQueueSet(Queue<E> q, Set<E> s) {
    this(q, s, -1);
  }
  
  /**
   * Creates a {@link FixedSizeQueueSet} with the given {@link Queue},
   * {@link Set} and max size.
   * 
   * <p>NOTE: The {@link FixedSizeQueueSet} does not check if the
   * initial state of the given arguments is correct.
   */
  public FixedSizeQueueSet(Queue<E> q, Set<E> s, int maxSize) {
    super(q, s);
    
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
  
  private void adjustSize() {
    if (maxSize != -1 && size() > maxSize) {
      E element = poll();
      removed(element);
    }
  }
  
  protected void removed(E e) {
    // Override
  }
  
  @Override
  public boolean offer(E e) {
    if (super.offer(e)) {
      adjustSize();
      return true;
    }
    return false;
  }
}