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

package org.ardverk.utils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A {@link Comparator} for {@link Comparable} objects.
 */
public class DefaultComparator<T extends Comparable<T>> 
        implements Comparator<T>, Serializable {
    
    private static final long serialVersionUID = 2605636601980606082L;
    
    @SuppressWarnings("rawtypes")
    private static final DefaultComparator COMPARATOR = new DefaultComparator();
    
    /**
     * Returns a {@link Comparator} for compare {@link Comparable} objects.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Comparator<T> create() {
        return (Comparator<T>)COMPARATOR;
    }
    
    @Override
    public int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }
}