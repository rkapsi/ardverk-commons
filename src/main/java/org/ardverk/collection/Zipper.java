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

package org.ardverk.collection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Zipper<E> implements Iterable<E>, Serializable {
    
    private static final long serialVersionUID = 8081213933323928016L;

    private static final Zipper<Object> EMPTY 
        = new Zipper<Object>(null, 0, null);
    
    @SuppressWarnings("unchecked")
    public static <E> Zipper<E> create() {
        return (Zipper<E>)EMPTY;
    }
    
    public static <E> Zipper<E> create(Iterable<? extends E> c) {
        Zipper<E> list = create();
        return list.addAll(c);
    }
    
    private final E element;
    
    private final int size;
    
    private final Zipper<E> next;
    
    private Zipper(E element, int size, Zipper<E> next) {
        this.element = element;
        this.size = size;
        this.next = next;
    }
    
    public E element() {
        if (this == EMPTY) {
            throw new NoSuchElementException();
        }
        
        return element;
    }
    
    public Zipper<E> tail() {
        return next;
    }
    
    public Zipper<E> add(E element) {
        return new Zipper<E>(element, size+1, this);
    }
    
    public Zipper<E> addAll(Iterable<? extends E> c) {
        Zipper<E> list = this;
        
        for (E element : c) {
            list = list.add(element);
        }
        
        return list;
    }
    
    public Zipper<E> remove(Object element) {
        Zipper<E> list = create();
        boolean found = false;
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            
            if (!found && PersistentUtils.equals(element, current.element)) {
                found = true;
                continue;
            }
            
            list = list.add(current.element);
        }
        
        return list;
    }
    
    public Zipper<E> without(Object element) {
        Zipper<E> list = create();
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            
            if (!PersistentUtils.equals(element, current.element)) {
                list = list.add(current.element);
            }
        }
        
        return list;
    }
    
    public Zipper<E> removeAll(Iterable<?> c) {
        Zipper<E> list = this;
        for (Object element : c) {
            if (list.isEmpty()) {
                break;
            }
            
            list = list.remove(element);
        }
        return list;
    }
    
    public Zipper<E> retainAll(Collection<?> c) {
        Zipper<E> list = create();
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            if (c.contains(current.element)) {
                list = list.add(current.element);
            }
        }
        
        return list;
    }
    
    public Zipper<E> retainAll(Zipper<?> c) {
        Zipper<E> list = create();
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            if (c.contains(current.element)) {
                list = list.add(current.element);
            }
        }
        
        return list;
    }
    
    public boolean contains(Object element) {
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            if (PersistentUtils.equals(element, current.element)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean containsAll(Iterable<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    public Zipper<E> flip() {
        Zipper<E> list = create();
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            list = list.add(current.element);
        }
        
        return list;
    }
    
    public Iterator<E> iterator() {
        return new ZipperItereator();
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public Zipper<E> clear() {
        return create();
    }
    
    public Object[] toArray() {
        return toArray(new Object[size]);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        T[] dst = a;
        if (dst.length < size) {
            dst = (T[])Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        
        int index = 0;
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            dst[index++] = (T)current.element;
        }
        
        return dst;
    }
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("[");
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.next) {
            buffer.append(current.element).append(", ");
        }
        
        // Remove the last comma
        if (buffer.length() >= 1) {
            buffer.setLength(buffer.length()-2);
        }
        
        buffer.append("]");
        return buffer.toString();
    }
    
    private class ZipperItereator implements Iterator<E> {
    
        private Zipper<E> current = Zipper.this;
        
        @Override
        public boolean hasNext() {
            return current != EMPTY;
        }
    
        @Override
        public E next() {
            if (current == EMPTY) {
                throw new NoSuchElementException();
            }
            
            E element = current.element;
            current = current.next;
            return element;
        }
    
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
