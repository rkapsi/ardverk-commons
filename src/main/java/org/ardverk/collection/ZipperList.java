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
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ZipperList<E> implements Collection<E>, Serializable {

    private static final long serialVersionUID = -8210279044829321415L;
    
    private volatile Zipper<E> zipper = Zipper.create();

    public ZipperList() {
    }
    
    public ZipperList(Collection<E> c) {
        addAll(c);
    }
    
    @Override
    public synchronized boolean add(E e) {
        zipper = zipper.add(e);
        return true;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
        zipper = zipper.addAll(c);
        return true;
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
        return zipper.containsAll(c);
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
        zipper = zipper.remove(o);
        // TODO: Don't really know if item was found
        return true;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        zipper = zipper.removeAll(c);
        // TODO: Don't really know if all items were found
        return true;
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        zipper = zipper.retainAll(c);
        // TODO: Don't really know if all items were found
        return true;
    }

    @Override
    public int size() {
        return zipper.size();
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
