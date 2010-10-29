/*
 * Copyright 2009 Roger Kapsi
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
 * Provides an interface to add, remove and retrieve {@link AsyncFutureListener}s.
 */
public interface AsyncFutureListenerService<V> {

    /**
     * Adds the given {@link AsyncFutureListener}.
     */
    public void addAsyncFutureListener(AsyncFutureListener<V> listener);
    
    /**
     * Removes the given {@link AsyncFutureListener}.
     */
    public void removeAsyncFutureListener(AsyncFutureListener<V> listener);
    
    /**
     * Returns all {@link AsyncFutureListener}s.
     */
    public AsyncFutureListener<V>[] getAsyncFutureListeners();
}
