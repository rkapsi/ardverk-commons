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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PersistentList<E> implements PersistentCollection<E> {
    
    private static final long serialVersionUID = 2441704703636813102L;

    private static final PersistentList<Object> EMPTY 
        = new PersistentList<Object>(null, 0, null);
    
    private final E element;
    
    private final int size;
    
    private final PersistentList<E> next;
    
    @SuppressWarnings("unchecked")
    public static <E> PersistentList<E> create() {
        return (PersistentList<E>)EMPTY;
    }
    
    public static <E> PersistentList<E> create(Iterable<? extends E> c) {
        PersistentList<E> list = create();
        return list.addAll(c);
    }
    
    private PersistentList(E element, int size, PersistentList<E> next) {
        this.element = element;
        this.size = size;
        this.next = next;
    }
    
    @Override
    public E element() {
        if (this == EMPTY) {
            throw new NoSuchElementException();
        }
        
        return element;
    }

    @Override
    public PersistentList<E> tail() {
        return next;
    }

    @Override
    public PersistentList<E> add(E element) {
        return new PersistentList<E>(element, size+1, this);
    }
    
    @Override
    public PersistentList<E> addAll(Iterable<? extends E> c) {
        PersistentList<E> list = this;
        
        if (c instanceof List<?>) {
            List<? extends E> src = (List<? extends E>)c;
            for (ListIterator<? extends E> it 
                    = src.listIterator(src.size()); it.hasPrevious(); ) {
                list = list.add(it.previous());
            }
        } else {
            for (E element : c) {
                list = list.add(element);
            }
        }
        
        return list;
    }
    
    @Override
    public PersistentList<E> remove(Object element) {
        PersistentList<E> list = create();
        boolean found = false;
        
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            
            if (!found && PersistentUtils.equals(element, current.element)) {
                found = true;
                continue;
            }
            
            list = list.add(current.element);
        }
        
        return list;
    }
    
    @Override
    public PersistentList<E> without(Object element) {
        PersistentList<E> list = create();
        
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            
            if (!PersistentUtils.equals(element, current.element)) {
                list = list.add(current.element);
            }
        }
        
        return list;
    }
    
    @Override
    public PersistentList<E> removeAll(Iterable<?> c) {
        PersistentList<E> list = this;
        for (Object element : c) {
            if (list.isEmpty()) {
                break;
            }
            
            list = list.remove(element);
        }
        return list;
    }
    
    @Override
    public PersistentList<E> retainAll(Collection<?> c) {
        PersistentList<E> list = create();
        
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            if (c.contains(current.element)) {
                list = list.add(current.element);
            }
        }
        
        return list;
    }
    
    @Override
    public PersistentList<E> retainAll(PersistentCollection<?> c) {
        PersistentList<E> list = create();
        
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            if (c.contains(current.element)) {
                list = list.add(current.element);
            }
        }
        
        return list;
    }
    
    @Override
    public boolean contains(Object element) {
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            if (PersistentUtils.equals(element, current.element)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean containsAll(Iterable<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    public PersistentList<E> flip() {
        PersistentList<E> list = create();
        
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            list = list.add(current.element);
        }
        
        return list;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListItereator();
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public PersistentList<E> clear() {
        return create();
    }
    
    @Override
    public Object[] toArray() {
        return toArray(new Object[size]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        T[] dst = a;
        if (dst.length < size) {
            dst = (T[])Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        
        int index = 0;
        for (PersistentList<E> current = this; 
                current != EMPTY; current = current.next) {
            dst[index++] = (T)current.element;
        }
        
        return dst;
    }
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("[");
        
        for (PersistentList<E> current = this; 
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

    private class ListItereator implements Iterator<E> {

        private PersistentList<E> current = PersistentList.this;
        
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
