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
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Zipper<E> implements Iterable<E>, Cloneable, Serializable {
    
    private static final long serialVersionUID = 8081213933323928016L;

    private static final Zipper<Object> EMPTY 
        = new Zipper<Object>(null, 0, null);
    
    @SuppressWarnings("unchecked")
    private static <E> Zipper<E> empty() {
        return (Zipper<E>)EMPTY;
    }
    
    public static <E> Zipper<E> create() {
        return empty();
    }
    
    public static <E> Zipper<E> create(Iterable<? extends E> c) {
        Zipper<E> zipper = create();
        
        for (E element : c) {
            zipper = zipper.add(element);
        }
        
        return zipper;
    }
    
    private final E element;
    
    private final int size;
    
    private volatile Zipper<E> tail;
    
    private Zipper(E element, int size, Zipper<E> tail) {
        this.element = element;
        this.size = size;
        this.tail = tail;
    }
    
    public E element() {
        if (this == EMPTY) {
            throw new NoSuchElementException();
        }
        
        return element;
    }
    
    public Zipper<E> tail() {
        return tail;
    }
    
    public Zipper<E> add(E element) {
        return new Zipper<E>(element, size+1, this);
    }
    
    /*public Zipper<E> remove(Object o) {
        Zipper<E> zipper = create();
        boolean modified = false;
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.tail()) {
            
            E element = current.element();
            if (!modified && equals(o, element)) {
                modified = true;
                continue;
            }
            
            zipper = zipper.add(element);
        }
        
        return modified ? zipper : this;
    }*/
    
    public Zipper<E> remove(Object o) {
        Zipper<E> zipper = null;
        Zipper<E> tail = null;
        
        boolean modified = false;
        
        int size = size();
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.tail()) {
            
            E element = current.element();
            if (!modified && equals(o, element)) {
                modified = true;
                continue;
            }
            
            if (zipper == null) {
                zipper = new Zipper<E>(element, --size, null);
                tail = zipper;
            } else {
                tail.tail = new Zipper<E>(element, --size, null);
                tail = tail.tail;
            }
        }
        
        if (modified && zipper != null) {
            tail.tail = empty(); // EOF
            return zipper;
        }
        
        return this;
    }
    
    public boolean contains(Object o) {
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.tail()) {
            E element = current.element();
            if (equals(o, element)) {
                return true;
            }
        }
        
        return false;
    }
    
    public Zipper<E> flip() {
        Zipper<E> zipper = create();
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.tail()) {
            zipper = zipper.add(current.element());
        }
        
        return zipper;
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
                current != EMPTY; current = current.tail()) {
            dst[index++] = (T)current.element();
        }
        
        return dst;
    }
    
    @Override
    public Zipper<E> clone() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("[");
        
        for (Zipper<E> current = this; 
                current != EMPTY; current = current.tail()) {
            buffer.append(current.element()).append(", ");
        }
        
        // Remove the last comma
        if (buffer.length() >= 1) {
            buffer.setLength(buffer.length()-2);
        }
        
        buffer.append("]");
        return buffer.toString();
    }
    
    private static boolean equals(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else if (b == null) {
            return false;
        }
        
        return a.equals(b);
    }

    private class ZipperItereator implements Iterator<E> {
    
        private Zipper<E> current = Zipper.this;
        
        private ZipperItereator() {
        }
        
        @Override
        public boolean hasNext() {
            return current != EMPTY;
        }
    
        @Override
        public E next() {
            if (current == EMPTY) {
                throw new NoSuchElementException();
            }
            
            E element = current.element();
            current = current.tail();
            
            return element;
        }
    
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
