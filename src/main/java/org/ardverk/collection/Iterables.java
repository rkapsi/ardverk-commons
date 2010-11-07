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
    public static <T> Iterable<T> create(T... values) {
        return create(values, 0, values.length);
    }
    
    /**
     * Creates an {@link Iterable} view for the given array.
     */
    public static <T> Iterable<T> create(final T[] values, 
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
                        return Iterators.create(values, offset, length);
                    }
                };
        }
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterator}s.
     */
    public static <T> Iterable<T> fromIterators(Iterator<T>... values) {
        return fromIterators(values, 0, values.length);
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterator}s.
     */
    public static <T> Iterable<T> fromIterators(Iterator<T>[] values, 
            int offset, final int length) {
        return fromIterators(create(values, offset, length));
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an {@link Iterable} of {@link Iterator}s.
     */
    public static <T> Iterable<T> fromIterators(final Iterable<? extends Iterator<T>> values) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.fromIterators(values.iterator());
            }
        };
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterable<T> fromIterables(Iterable<T>... values) {
        return fromIterables(values, 0, values.length);
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an array of {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterable<T> fromIterables(Iterable<T>[] values, 
            int offset, int length) {
        return fromIterables(create(values, offset, length));
    }
    
    /**
     * Creates and returns a composed {@link Iterable} view from 
     * an {@link Iterable} of {@link Iterable}s such as {@link List}s.
     */
    public static <T> Iterable<T> fromIterables(final Iterable<? extends Iterable<T>> values) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.fromIterables(values.iterator());
            }
        };
    }
}