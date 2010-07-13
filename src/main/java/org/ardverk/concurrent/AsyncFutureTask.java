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
    
    private static final Callable<Object> NOP = new Callable<Object>() {
        @Override
        public Object call() {
            throw new UnsupportedOperationException("Please override doRun()");
        }
    };
    
    private final AtomicReference<Interruptible> thread 
        = new AtomicReference<Interruptible>(Interruptible.INIT);
    
    private final Callable<V> callable;
    
    /**
     * Creates an {@link AsyncFutureTask}
     */
    @SuppressWarnings("unchecked")
    public AsyncFutureTask() {
        this((Callable<V>)NOP);
    }
    
    /**
     * Creates an {@link AsyncFutureTask} with the 
     * given {@link Runnable} and return value
     */
    public AsyncFutureTask(Runnable task, V value) {
        this(Executors.callable(task, value));
    }
    
    /**
     * Creates an {@link AsyncFutureTask} with the given
     * {@link Callable}.
     */
    public AsyncFutureTask(Callable<V> callable) {
        if (callable == null) {
            throw new NullArgumentException("callable");
        }
        
        this.callable = callable;
    }
    
    @Override
    public final void run() {
        if (preRun()) {
            try {
                doRun();
            } catch (Throwable t) {
                uncaughtException(t);
            } finally {
                postRun();
            }
        }
    }
    
    /**
     * Called before {@link #doRun()}
     */
    private boolean preRun() {
        return thread.compareAndSet(Interruptible.INIT, new CurrentThread());
    }
    
    /**
     * Called after {@link #doRun()}
     */
    private void postRun() {
        // See cancel() for the reasoning why we're using synchronized here!
        synchronized (thread) {
            thread.set(Interruptible.DONE);
        }
    }
    
    /**
     * Called by the {@link AsyncFutureTask}'s {@link #run()} method.
     * You may override this method for custom implementations.
     */
    protected void doRun() throws Exception {
        if (!isDone()) {
            V value = callable.call();
            setValue(value);
        }
    }
    
    /**
     * This method is being called if an uncaught {@link Exception}
     * occurred in {@link #doRun()} and {@link #run()} respectively.
     * The default implementation will pass it on to 
     * {@link #setException(Throwable)}.
     */
    protected void uncaughtException(Throwable t) {
        setException(t);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean success = super.cancel(mayInterruptIfRunning);
        
        if (success && mayInterruptIfRunning) {
            
            // NOTE: We must use synchronized here to make sure we're
            // not interrupting an another task. Say a Thread starts
            // to execute this task, we call cancel(true) but haven't
            // called interrupt() yet. The Thread moves on to execute
            // the next task and we call interrupt. In other words,
            // AtomicReference.getAndSet() is atomic but the interrupt()
            // method call is not part of the atomic operation.
            
            synchronized (thread) {
                thread.getAndSet(Interruptible.DONE).interrupt();
            }
        }
        
        return success;
    }
}
