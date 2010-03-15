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
import java.util.List;
import java.util.ListIterator;

public class PersistentUtils {

    private PersistentUtils() {}
    
    public static <E> Collection<E> asCollection(PersistentList<E> list) {
        if (list == null) {
            throw new NullPointerException("list");
        }
        
        return new CollectionDelegate<E>(list);
    }
    
    public static <E> List<E> asList(PersistentList<E> list) {
        if (list == null) {
            throw new NullPointerException("list");
        }
        
        return new ListDelegate<E>(list);
    }
    
    static boolean equals(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else if (b == null) {
            return false;
        }
        
        return a.equals(b);
    }
    
    private static class CollectionDelegate<E> implements Collection<E>, Serializable {
        
        private static final long serialVersionUID = -3210871398742336958L;
        
        protected volatile PersistentList<E> list;
        
        public CollectionDelegate(PersistentList<E> list) {
            this.list = list;
        }

        @Override
        public boolean add(E e) {
            list = list.add(e);
            return true;
        }
        
        @Override
        public boolean addAll(Collection<? extends E> c) {
            list = list.addAll(c);
            return true;
        }

        @Override
        public void clear() {
            list = list.clear();
        }

        @Override
        public boolean contains(Object o) {
            return list.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return list.containsAll(c);
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public Iterator<E> iterator() {
            return list.iterator();
        }
        
        @Override
        public boolean remove(Object o) {
            list = list.remove(o);
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            list = list.removeAll(c);
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            list = list.retainAll(c);
            return false;
        }
        
        @Override
        public int size() {
            return list.size();
        }

        @Override
        public Object[] toArray() {
            return list.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return list.toArray(a);
        }
        
        @Override
        public String toString() {
            return list.toString();
        }
    }
    
    private static class ListDelegate<E> extends CollectionDelegate<E> implements List<E> {
        
        private static final long serialVersionUID = 2413964511804930738L;

        public ListDelegate(PersistentList<E> list) {
            super(list);
        }

        @Override
        public void add(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E get(int index) {
            int idx = size() - index;
            for (E element : list) {
                if (idx-- == 0) {
                    return element;
                }
            }
            throw new IndexOutOfBoundsException("index=" + index);
        }

        @Override
        public int indexOf(Object o) {
            int index = 0;
            for (E element : list) {
                if (PersistentUtils.equals(element, o)) {
                    return size() - index;
                }
                
                ++index;
            }
            
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            int index = -1;
            
            int i = 0;
            for (E element : list) {
                if (PersistentUtils.equals(element, o)) {
                    index = size() - i;
                }
                
                ++i;
            }
            
            return index;
        }

        @Override
        public ListIterator<E> listIterator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E remove(int index) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return null;
        }
    }
}
