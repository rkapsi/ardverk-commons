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

import java.util.concurrent.atomic.AtomicReference;

/**
 * A special purpose version of an {@link AtomicReference}.
 */
public class ValueReference<V> extends AtomicReference<V> {
    
    private static final long serialVersionUID = -1871652270359986302L;

    public ValueReference() {
        super();
    }

    public ValueReference(V initialValue) {
        super(initialValue);
    }
    
    /**
     * Calls the {@link #set(Object)} on the {@link AtomicReference} and
     * returns the value.
     * 
     * <p>IMPORTANT NOTE: This operation is not totally atomic and is meant
     * to be a convenience method for the following scenario.
     * 
     * final AsyncAtomicReference<SomeObject> valueRef 
     *     = new AsyncAtomicReference<SomeObject>();
     * SwingUtilities.invokeAndWait(new Runnable() {
     *     public void run() {
     *         SomeObject obj = valueRef.make(new SomeObject());
     *         obj.doSomething();
     *     }
     * });
     * 
     * valueRef.get().doSomethingElse();
     */
    public V make(V newValue) {
        set(newValue);
        return newValue;
    }
}
