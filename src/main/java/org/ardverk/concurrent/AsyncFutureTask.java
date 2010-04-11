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

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

import org.ardverk.lang.NullArgumentException;

/**
 * A cancellable asynchronous computation. This class provides a base
 * implementation of {@link AsyncFuture}, with methods to start and 
 * cancel a computation, query to see if the computation is complete, 
 * and retrieve the result of the computation.
 * 
 * @see FutureTask
 */
public class AsyncFutureTask<V> extends AsyncValueFuture<V> 
        implements AsyncRunnableFuture<V> {
    
    private final Callable<V> callable;
    
    private final AtomicReference<Interruptible> thread 
        = new AtomicReference<Interruptible>(Interruptible.INIT);
    
    public AsyncFutureTask(Runnable task, V value) {
        this(Executors.callable(task, value));
    }
    
    public AsyncFutureTask(Callable<V> callable) {
        if (callable == null) {
            throw new NullArgumentException("callable");
        }
        
        this.callable = callable;
    }
    
    @Override
    public void run() {
        if (thread.compareAndSet(Interruptible.INIT, new CurrentThread())) {
            try {
                if (!isDone()) {
                    try {
                        V value = callable.call();
                        setValue(value);
                    } catch (Exception err) {
                        setException(err);
                    }
                }
            } finally {
                thread.set(Interruptible.DONE);
            }
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean success = super.cancel(mayInterruptIfRunning);
        
        if (success && mayInterruptIfRunning) {
            thread.getAndSet(Interruptible.DONE).interrupt();
        }
        
        return success;
    }
}
