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

package org.ardverk.utils;

import java.io.Serializable;
import java.util.Comparator;

import org.ardverk.lang.Arguments;

/**
 * A {@link ReverseComparator} takes a {@link Comparator} as input and reverses
 * the {@link Comparator}'s output. It's useful for changing the sorting order
 * for example.
 */
public class ReverseComparator<T> implements Comparator<T>, Serializable {
    
    private static final long serialVersionUID = 563094817278176159L;
    
    private final Comparator<? super T> comparator;
    
    public ReverseComparator(Comparator<? super T> comparator) {
        this.comparator = Arguments.notNull(comparator, "comparator");
    }
    
    /**
     * Returns the {@link ReverseComparator}'s parent {@link Comparator}.
     */
    public Comparator<? super T> getComparator() {
        return comparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return -comparator.compare(o1, o2);
    }
}
