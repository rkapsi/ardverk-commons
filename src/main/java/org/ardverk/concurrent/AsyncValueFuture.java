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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ardverk.lang.NullArgumentException;

public class AsyncValueFuture<V> implements AsyncFuture<V> {

    protected final AsyncExchanger<V, ExecutionException> exchanger 
        = new AsyncExchanger<V, ExecutionException>(this);
    
    /**
     * The first {@link AsyncFutureListener} that was added *before* the 
     * {@link AsyncFuture} completed. 
     */
    private AsyncFutureListener<V> first = null;
    
    /**
     * All {@link AsyncFutureListener}s that were added *before* the 
     * {@link AsyncFuture} completed.
     */
    private List<AsyncFutureListener<V>> before = null;
    
    /**
     * All {@link FooListener}s that were added *after* the 
     * {@link FooFuture} completed.
     */
    private List<AsyncFutureListener<V>> after = null;
    
    /**
     * Creates an {@link AsyncValueFuture}.
     */
    public AsyncValueFuture() {
        
    }
    
    /**
     * Creates an {@link AsyncValueFuture} with the given value.
     */
    public AsyncValueFuture(V value) {
        setValue(value);
    }
    
    /**
     * Creates and {@link AsyncValueFuture} with the given {@link Throwable}.
     */
    public AsyncValueFuture(Throwable t) {
        setException(t);
    }
    
    @Override
    public void addAsyncFutureListener(AsyncFutureListener<V> listener) {
        if (listener == null) {
            throw new NullArgumentException("listener");
        }
        
        boolean done = false;
        
        synchronized (this) {
            done = isDone();
            
            if (done) {
                if (after == null) {
                    after = new ArrayList<AsyncFutureListener<V>>();
                }
                
                after.add(listener);
            } else {
                
                if (first == null) {
                    first = listener;
                } else {
                    if (before == null) {
                        before = new ArrayList<AsyncFutureListener<V>>();
                    }
                    
                    before.add(listener);
                }
            }
        }
        
        if (done) {
            fireOperationComplete(listener);
        }
    }

    @Override
    public synchronized void removeAsyncFutureListener(AsyncFutureListener<V> listener) {
        if (listener == null) {
            throw new NullArgumentException("listener");
        }
        
        if (first == listener) {
            if (before == null || before.isEmpty()) {
                first = null;
            } else {
                first = before.remove(0);
            }
        } else {
            
            boolean success = false;
            if (before != null) {
                success = before.remove(listener);
            }
            
            if (!success && after != null) {
                after.remove(listener);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public synchronized AsyncFutureListener<V>[] getAsyncFutureListeners() {
        List<AsyncFutureListener<V>> listeners = new ArrayList<AsyncFutureListener<V>>();
        
        if (first != null) {
            listeners.add(first);
        }
        
        if (before != null) {
            listeners.addAll(before);
        }
        
        if (after != null) {
            listeners.addAll(after);
        }
        
        return listeners.toArray(new AsyncFutureListener[0]);
    }

    @Override
    public boolean setException(Throwable t) {
        boolean success = exchanger.setException(wrap(t));
        
        if (success) {
            complete();
        }
        
        return success;
    }

    @Override
    public boolean setValue(V value) {
        boolean success = exchanger.setValue(value);
        
        if (success) {
            complete();
        }
        
        return success;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean success = exchanger.cancel();
        
        if (success) {
            complete();
        }
        
        return success;
    }
    
    @Override
    public V tryGet() throws InterruptedException, ExecutionException {
        return exchanger.tryGet();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return exchanger.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return exchanger.get(timeout, unit);
    }

    @Override
    public boolean isCancelled() {
        return exchanger.isCancelled();
    }

    @Override
    public boolean isDone() {
        return exchanger.isDone();
    }
    
    @Override
    public boolean isCompletedAbnormally() {
        return exchanger.isCompletedAbnormally();
    }
    
    /**
     * 
     */
    private void complete() {
        fireOperationComplete();
        done();
    }
    
    /**
     * 
     */
    protected void done() {
        
    }
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    private void fireOperationComplete() {
        AsyncFutureListener<V> first = null;
        AsyncFutureListener<V>[] others = null;
        
        synchronized (this) {
            first = this.first;
            
            if (before != null) {
                others = before.toArray(new AsyncFutureListener[0]);
            }
        }
        
        fireOperationComplete(first, others);
    }
    
    /**
     * 
     */
    protected void fireOperationComplete(AsyncFutureListener<V> first, 
            AsyncFutureListener<V>... others) {
        
        if (first != null) {
            first.operationComplete(this);
        }
        
        if (others != null) {
            for (AsyncFutureListener<V> l : others) {
                l.operationComplete(this);
            }
        }
    }
    
    /**
     * 
     */
    private static ExecutionException wrap(Throwable t) {
        if (t instanceof ExecutionException) {
            return (ExecutionException)t;
        }
        
        return new ExecutionException(t);
    }
}
