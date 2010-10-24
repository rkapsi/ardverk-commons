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

package org.ardverk.concurrent;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicUtils {

    private AtomicUtils() {}
    
    /**
     * An utility method to set an {@link AtomicReference}'s value
     * and immediately return it.
     * 
     * IMPORTANT NOTE: This is primarily a convenience operation
     * rather than an atomic operation.
     * 
     * final AtomicReference<SomeObject> valueRef 
     *     = new AtomicReference<SomeObject>();
     * SwingUtilities.invokeAndWait(new Runnable() {
     *     public void run() {
     *         SomeObject obj = AtomicUtils.set(valueRef, new SomeObject());
     *         obj.doSomething();
     *     }
     * });
     * 
     * valueRef.get().doSomethingElse();
     */
    public static <V> V set(AtomicReference<? super V> valueRef, V value) {
        valueRef.set(value);
        return value;
    }
    
    /**
     * Same idea as {@link #set(AtomicReference, Object)} but for arrays.
     */
    public static <V> V set(V[] valueRef, V value) {
        valueRef[0] = value;
        return value;
    }
    
    /**
     * Same idea as {@link #set(AtomicReference, Object)} but for {@link Collection}s.
     */
    public static <V> V add(Collection<? super V> valueRef, V value) {
        valueRef.add(value);
        return value;
    }
}
