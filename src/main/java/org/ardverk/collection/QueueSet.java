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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.ardverk.lang.NullArgumentException;

/**
 * 
 */
public class QueueSet<E> implements Queue<E>, Set<E>, Serializable {
    
    private static final long serialVersionUID = 1692909657689736799L;

    protected final Queue<E> q;
    
    protected final Set<E> s;
    
    public QueueSet() {
        this(new LinkedList<E>(), new HashSet<E>());
    }
    
    public QueueSet(Queue<E> q, Set<E> s) {
        if (q == null) {
            throw new NullArgumentException("q");
        }
        
        if (s == null) {
            throw new NullArgumentException("s");
        }
        
        this.q = q;
        this.s = s;
    }
    
    @Override
    public boolean add(E e) {
        return offer(e);
    }
    
    @Override
    public boolean offer(E e) {
        boolean success = false;
        try {
            success = offer(s.add(e), e);
        } finally {
            if (!success) {
                s.remove(e);
            }
        }
        return success;
    }
    
    protected boolean offer(boolean unique, E e) {
        if (unique) {
            return q.offer(e);
        }
        return false;
    }

    @Override
    public E element() {
        return q.element();
    }
    
    @Override
    public E peek() {
        return q.peek();
    }

    @Override
    public E poll() {
        E element = q.poll();
        s.remove(element);
        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    @Override
    public boolean isEmpty() {
        return q.isEmpty();
    }
    
    @Override
    public int size() {
        return q.size();
    }
    
    @Override
    public void clear() {
        q.clear();
        s.clear();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }
    
    @Override
    public E remove() {
        E element = q.remove();
        s.remove(element);
        return element;
    }
    
    @Override
    public boolean remove(Object o) {
        if (s.remove(o)) {
            return q.remove(o);
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            modified |= add(element);
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            modified |= remove(element);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        
        for (Iterator<E> it = iterator(); it.hasNext(); ) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        
        return modified;
    }
    
    @Override
    public Object[] toArray() {
        return q.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return q.toArray(a);
    }
    
    @Override
    public String toString() {
        return q.toString();
    }
    
    /**
     * 
     */
    private class QueueIterator implements Iterator<E> {
        
        private final Iterator<E> it = q.iterator();

        private E current = null;
        
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public E next() {
            current = it.next();
            return current;
        }

        @Override
        public void remove() {
            it.remove();
            s.remove(current);
            current = null;
        }
    }
}
