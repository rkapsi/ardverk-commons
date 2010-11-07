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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An utility class to create light-weight instances of {@link Iterator}s.
 */
public class Iterators {
    
    private Iterators() {}
    
    /**
     * An {@link Iterator} that has no elements.
     */
    public static final Iterator<Object> EMPTY = new Iterator<Object>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
    
    /**
     * Returns an empty {@link Iterator}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> empty() {
        return (Iterator<T>)EMPTY;
    }
    
    /**
     * Creates an {@link Iterator} view for the value.
     */
    public static <T> Iterator<T> singleton(T value) {
        return new SingletonIterator<T>(value);
    }
    
    /**
     * Creates an {@link Iterator} view for the given array.
     */
    public static <T> Iterator<T> create(T... values) {
        return create(values, 0, values.length);
    }
    
    /**
     * Creates an {@link Iterator} view for the given array.
     */
    public static <T> Iterator<T> create(T[] values, int offset, int length) {
        switch (length) {
            case 0:
                return empty();
            case 1:
                return singleton(values[offset]);
            default:
                return new ArrayIterator<T>(values, offset, length);
        }
    }
    
    /**
     * Creates and returns an {@link Iterator} from a composed view of {@link Iterator}s.
     */
    public static <T> Iterator<T> fromIterators(Iterator<T>... values) {
        return fromIterators(values, 0, values.length);
    }
    
    /**
     * Creates and returns an {@link Iterator} from a composed view of {@link Iterator}s.
     */
    public static <T> Iterator<T> fromIterators(Iterator<T>[] values, int offset, int length) {
        return fromIterators(create(values, offset, length));
    }
    
    /**
     * Creates and returns an {@link Iterator} from a composed view of {@link Iterator}s.
     */
    public static <T> Iterator<T> fromIterators(Iterator<? extends Iterator<T>> values) {
        if (!values.hasNext()) {
            return empty();
        }
        
        return new IteratorIterator<T>(values);
    }
    
    /**
     * Creates and returns an {@link Iterator} from a composed view of 
     * {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterator<T> fromIterables(Iterable<T>... values) {
        return fromIterables(values, 0, values.length);
    }
    
    /**
     * Creates and returns an {@link Iterator} from a composed view of 
     * {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterator<T> fromIterables(Iterable<T>[] values, int offset, int length) {
        return fromIterables(create(values, offset, length));
    }
    
    /**
     * Creates and returns an {@link Iterator} from a composed view of 
     * {@link Iterables}s such as {@link List}s.
     */
    public static <T> Iterator<T> fromIterables(Iterator<? extends Iterable<T>> values) {
        if (!values.hasNext()) {
            return empty();
        }
        
        return new ItarableIterator<T>(values);
    }
    
    /**
     * An {@link Iterator} that is a composition of other {@link Iterator}s.
     */
    private static abstract class Composition<T> implements Iterator<T> {
        
        protected Iterator<T> current = null;
        
        protected Iterator<T> item = null;
        
        @Override
        public boolean hasNext() {
            return current != null && current.hasNext();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            T value = current.next();
            
            // Remember the Iterator of the current element. 
            // We need it for the remove() operation.
            item = current; 
            
            // Advance to the next Iterator in the list if necessary.
            advance();
            
            return value;
        }

        @Override
        public void remove() {
            if (item == null) {
                throw new IllegalStateException();
            }
            
            item.remove();
            item = null;
        }
        
        /**
         * Called for each {@link #next()}.
         */
        protected abstract void advance();
    }
    
    /**
     * An implementation of {@link Composition} that iterates over
     * instances of {@link Iterator}.
     */
    private static class IteratorIterator<T> extends Composition<T> {

        private final Iterator<? extends Iterator<T>> values;
        
        public IteratorIterator(Iterator<? extends Iterator<T>> values) {
            this.values = values;
            advance();
        }
        
        @Override
        protected void advance() {
            while ((current == null || !current.hasNext()) 
                    && values.hasNext()) {
                current = values.next();
            }
        }
    }
    
    /**
     * An implementation of {@link Composition} that iterates over
     * instances of {@link Iterable}s such as {@link List}s.
     */
    private static class ItarableIterator<T> extends Composition<T> {

        private final Iterator<? extends Iterable<T>> values;
        
        public ItarableIterator(Iterator<? extends Iterable<T>> values) {
            this.values = values;
            advance();
        }
        
        @Override
        protected void advance() {
            while ((current == null || !current.hasNext()) 
                    && values.hasNext()) {
                current = values.next().iterator();
            }
        }
    }
}
