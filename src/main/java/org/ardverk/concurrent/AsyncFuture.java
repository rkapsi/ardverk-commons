/*
 * Copyright 2009, 2010 Roger Kapsi
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * An {@link AsyncFuture} represents the result of an asynchronously
 * scheduled (executed) computation.
 * 
 * @see Future
 */
public interface AsyncFuture<V> extends Future<V>, Cancellable {
    
    /**
     * Sets the value of the {@link AsyncFuture}
     */
    public boolean setValue(V value);
    
    /**
     * Sets the {@link Exception} of the {@link AsyncFuture}
     */
    public boolean setException(Throwable t);
    
    /**
     * A non-blocking version of the {@link #get()} method. It returns
     * either null if the {@link AsyncFuture} is not done yet or the
     * value and throws an {@link Exception} respectively.
     */
    public V tryGet() throws InterruptedException, ExecutionException;
    
    /**
     * Returns true if the {@link AsyncFuture} is done and completed 
     * abnormally and a subsequent {@link #get()} call will throw 
     * an {@link Exception}.
     */
    public boolean isCompletedAbnormally();
    
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
