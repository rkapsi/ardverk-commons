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

public interface PersistentCollection<E> extends Iterable<E>, Serializable {

    public PersistentCollection<E> add(E element);
    
    public PersistentCollection<E> addAll(Iterable<? extends E> c);
    
    public PersistentCollection<E> remove(Object element);
    
    public PersistentCollection<E> removeAll(Iterable<?> c);
    
    public PersistentCollection<E> retainAll(Collection<?> c);
    
    public PersistentCollection<E> retainAll(PersistentCollection<?> c);
    
    public PersistentCollection<E> without(Object element);
    
    public E element();
    
    public PersistentCollection<E> tail();
    
    public boolean contains(Object element);
    
    public boolean containsAll(Iterable<?> c);
    
    public int size();
    
    public boolean isEmpty();
    
    public PersistentCollection<E> clear();
    
    public Object[] toArray();
    
    public <T> T[] toArray(T[] dst);
}
