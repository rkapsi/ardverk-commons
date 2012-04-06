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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Insert: O(1)
 * Remove: O(n)
 * 
 * http://en.wikipedia.org/wiki/Persistent_data_structure
 * http://en.wikipedia.org/wiki/Zipper_(data_structure)
 */
public class ZipperList<E> implements List<E>, Cloneable, Serializable {

  private static final long serialVersionUID = -8210279044829321415L;
  
  private volatile Zipper<E> zipper = Zipper.create();

  public ZipperList() {
  }
  
  public ZipperList(Collection<E> c) {
    if (c == null) {
      throw new NullPointerException("c");
    }
    
    addAll(c);
  }
  
  private ZipperList(Zipper<E> zipper) {
    if (zipper == null) {
      throw new NullPointerException("zipper");
    }
    
    this.zipper = zipper;
  }
  
  @Override
  public synchronized boolean add(E element) {
    zipper = zipper.add(element);
    return true;
  }
  
  @Override
  public synchronized void add(int index, E element) {
    int size = size();
    if (index < 0 || size < index) {
      throw new IndexOutOfBoundsException("index=" + index);
    }
    
    if (index != size) {
      throw new UnsupportedOperationException();
    }
    
    add(element);
  }

  @Override
  public synchronized boolean addAll(Collection<? extends E> c) {
    boolean modified = false;
    for (E element : c) {
      if (add(element)) {
        modified = true;
      }
    }
    return modified;
  }
  
  @Override
  public synchronized boolean addAll(int index, Collection<? extends E> c) {
    int size = size();
    if (index < 0 || size < index) {
      throw new IndexOutOfBoundsException("index=" + index);
    }
    
    if (index != size) {
      throw new UnsupportedOperationException();
    }
    
    return addAll(c);
  }

  @Override
  public E get(int index) {
    Zipper<E> zipper = this.zipper;
    if (index < 0 || zipper.size() < index) {
      throw new IndexOutOfBoundsException("index=" + index);
    }
    
    for (E element : zipper) {
      if (index == 0) {
        return element;
      }
      --index;
    }
    
    throw new IllegalStateException();
  }

  @Override
  public int indexOf(Object o) {
    int index = 0;
    for (E element : zipper) {
      if (Zipper.equals(o, element)) {
        return index;
      }
      ++index;
    }
    
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    int index = 0;
    int lastIndex = -1;
    for (E element : zipper) {
      if (Zipper.equals(o, element)) {
        lastIndex = index;
      }
      ++index;
    }
    
    return lastIndex;
  }

  @Override
  public ListIterator<E> listIterator() {
    return new ListIterator<E>() {
      
      private final Iterator<E> it = iterator();

      @Override
      public void add(E e) {
        ZipperList.this.add(e);
      }

      @Override
      public boolean hasNext() {
        return it.hasNext();
      }

      @Override
      public boolean hasPrevious() {
        return false;
      }

      @Override
      public E next() {
        return it.next();
      }

      @Override
      public int nextIndex() {
        throw new UnsupportedOperationException();
      }

      @Override
      public E previous() {
        throw new UnsupportedOperationException();
      }

      @Override
      public int previousIndex() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void remove() {
        it.remove();
      }

      @Override
      public void set(E e) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public synchronized ListIterator<E> listIterator(int index) {
    if (index < 0 || size() < index) {
      throw new IndexOutOfBoundsException("index=" + index);
    }
    
    if (index != 0) {
      throw new UnsupportedOperationException();
    }
    
    return listIterator();
  }

  @Override
  public synchronized E remove(int index) {
    if (index < 0 || size() < index) {
      throw new IndexOutOfBoundsException("index=" + index);
    }
    
    E element = null;
    for (Iterator<E> it = iterator(); it.hasNext(); ) {
      element = it.next();
      if (index == 0) {
        it.remove();
        break;
      }
      --index;
    }
    
    return element;
  }

  @Override
  public E set(int index, E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public synchronized void clear() {
    zipper = zipper.clear();
  }

  @Override
  public boolean contains(Object o) {
    return zipper.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }
    return true;
  }
  
  @Override
  public boolean isEmpty() {
    return zipper.isEmpty();
  }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      
      private final Iterator<E> it = zipper.iterator();

      private E element = null;
      
      private boolean flag = false;
      
      @Override
      public boolean hasNext() {
        return it.hasNext();
      }

      @Override
      public E next() {
        element = it.next();
        flag = true;
        return element;
      }

      @Override
      public void remove() {
        if (!flag) {
          throw new NoSuchElementException();
        }
        
        flag = false;
        ZipperList.this.remove(element);
      }
    };
  }

  @Override
  public synchronized boolean remove(Object o) {
    Zipper<E> after = zipper.remove(o);
    if (after != zipper) {
      zipper = after;
      return true;
    }
    return false;
  }

  @Override
  public synchronized boolean removeAll(Collection<?> c) {
    boolean modified = false;
    for (Iterator<?> it = iterator(); it.hasNext(); ) {
      if (c.contains(it.next())) {
        it.remove();
        modified = true;
      }
    }
    return modified;
  }

  @Override
  public synchronized boolean retainAll(Collection<?> c) {
    boolean modified = false;
    for (Iterator<?> it = iterator(); it.hasNext(); ) {
      if (!c.contains(it.next())) {
        it.remove();
        modified = true;
      }
    }
    return modified;
  }

  @Override
  public int size() {
    return zipper.size();
  }
  
  @Override
  public ZipperList<E> clone() {
    return new ZipperList<E>(zipper);
  }
  
  @Override
  public Object[] toArray() {
    return zipper.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return zipper.toArray(a);
  }
  
  @Override
  public String toString() {
    return zipper.toString();
  }
}