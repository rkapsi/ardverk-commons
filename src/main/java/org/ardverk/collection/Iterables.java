/*
 * Copyright 2010-2011 Roger Kapsi
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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An utility class to create light-weight instances of {@link Iterable}s.
 */
public class Iterables {

    private Iterables() {}
    
    /**
     * An {@link Iterable} that is empty (i.e. its {@link Iterable#iterator()}
     * will return an {@link Iterator} that has no elements).
     */
    public static final Iterable<Object> EMPTY = new Iterable<Object>() {
        @Override
        public Iterator<Object> iterator() {
            return Iterators.EMPTY;
        }
    };
    
    /**
     * Creates an empty {@link Iterable} view.
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> empty() {
        return (Iterable<T>)EMPTY;
    }
    
    /**
     * Creates an {@link Iterable} view for the value.
     */
    public static <T> Iterable<T> singleton(final T value) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.singleton(value);
            }
        };
    }
    
    /**
     * Creates an {@link Iterable} view for the given array.
     */
    public static <T> Iterable<T> fromArray(T... values) {
        return fromArray(values, 0, values.length);
    }
    
    /**
     * Creates an {@link Iterable} view for the given array.
     */
    public static <T> Iterable<T> fromArray(final T[] values, 
            final int offset, final int length) {
        
        switch (length) {
            case 0:
                return empty();
            case 1:
                return singleton(values[offset]);
            default:
                return new Iterable<T>() {
                    @Override
                    public Iterator<T> iterator() {
                        return Iterators.fromArray(values, offset, length);
                    }
                };
        }
    }
    
    /**
     * Creates and returns an {@link Iterable} for an {@link Iterator}.
     */
    public static <T> Iterable<T> fromIterator(final Iterator<T> it) {
        if (!it.hasNext()) {
            return empty();
        }
        
        return new OneTimeIterable<T>() {
            @Override
            protected Iterator<T> iterator0() {
                return it;
            }
        };
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterator}s.
     */
    public static <T> Iterable<T> fromIterators(Iterator<? extends T>... values) {
        return fromIterators(values, 0, values.length);
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterator}s.
     */
    public static <T> Iterable<T> fromIterators(Iterator<? extends T>[] values, 
            int offset, final int length) {
        if (length == 0) {
            return empty();
        }
        
        return fromIterators(fromArray(values, offset, length));
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an {@link Iterable} of {@link Iterator}s.
     */
    public static <T> Iterable<T> fromIterators(final Iterable<? extends Iterator<? extends T>> values) {
        return new OneTimeIterable<T>() {
            @Override
            protected Iterator<T> iterator0() {
                return Iterators.fromIterators(values.iterator());
            }
        };
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterable<T> fromIterables(Iterable<? extends T>... values) {
        return fromIterables(values, 0, values.length);
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterable<T> fromIterables(Iterable<? extends T>[] values, 
            int offset, int length) {
        
        if (length == 0) {
            return empty();
        }
        
        Iterable<? extends Iterable<? extends T>> iterables 
            = fromArray(values, offset, length);
        
        return fromIterables(iterables);
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an {@link Iterable} of {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterable<T> fromIterables(final Iterable<? extends Iterable<? extends T>> values) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.fromIterables(values.iterator());
            }
        };
    }
    
    /**
     * An {@link Iterable} that can be used only once.
     */
    private static abstract class OneTimeIterable<T> implements Iterable<T> {
        
        private final AtomicBoolean state = new AtomicBoolean(false);

        @Override
        public Iterator<T> iterator() {
            if (state.getAndSet(true)) {
                throw new IllegalStateException();
            }
            
            return iterator0();
        }
        
        /**
         * @see Iterable#iterator()
         */
        protected abstract Iterator<T> iterator0();
    }
}