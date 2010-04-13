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

/**
 * An interface for interruptible objects such as {@link Thread}s.
 */
public interface Interruptible {
    
    /**
     * An initial state for {@link Interruptible} objects.
     */
    public static final Interruptible INIT = new Interruptible() {
        @Override
        public void interrupt() {
            // Do nothing!
        }
    };
    
    /**
     * A final state for {@link Interruptible} objects.
     */
    public static final Interruptible DONE = new Interruptible() {
        @Override
        public void interrupt() {
            // Do nothing!
        }
    };
    
    /**
     * Interrupts the execution of the object.
     */
    public void interrupt();
}