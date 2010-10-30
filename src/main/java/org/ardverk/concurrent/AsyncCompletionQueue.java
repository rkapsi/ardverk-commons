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

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.ardverk.collection.IdentityHashSet;
import org.ardverk.lang.Arguments;

/**
 * @see CompletionService
 */
public class AsyncCompletionQueue<V> implements AsyncFutureListenerService<V>, Closeable {
    
    private final AsyncFutureListener<V> completionListener 
            = new AsyncFutureListener<V>() {
        @Override
        public void operationComplete(AsyncFuture<V> future) {
            AsyncCompletionQueue.this.operationComplete(future);
        }
    };
    
    private final List<AsyncFutureListener<V>> listeners 
        = new CopyOnWriteArrayList<AsyncFutureListener<V>>();
    
    private final Set<AsyncFuture<V>> futures 
        = new IdentityHashSet<AsyncFuture<V>>();
    
    private final List<AsyncFuture<V>> complete 
        = new ArrayList<AsyncFuture<V>>();
    
    private boolean open = true;
    
    @Override
    public synchronized void close() {
        open = false;
        
        cancelAll(true);
        notifyAll();
    }
    
    /**
     * 
     */
    public synchronized int cancelAll(boolean mayInterruptIfRunning) {
        for (AsyncFuture<V> future : futures) {
            future.cancel(mayInterruptIfRunning);
        }
        
        return futures.size();
    }
    
    /**
     * 
     */
    public synchronized boolean offer(AsyncFuture<V> future) 
            throws RejectedExecutionException {
        
        if (!open) {
            throw new RejectedExecutionException();
        }
        
        boolean success = futures.add(future);
        if (success) {
            future.addAsyncFutureListener(completionListener);
        }
        return success;
    }
    
    /**
     * 
     */
    private synchronized void operationComplete(AsyncFuture<V> future) {
        boolean success = futures.remove(future);
        if (success) {
            future.removeAsyncFutureListener(completionListener);
            
            for (AsyncFutureListener<V> listener : listeners) {
                listener.operationComplete(future);
            }
            
            complete.add(future);
            notifyAll();
        }
    }
    
    /**
     * 
     */
    public synchronized AsyncFuture<V> take() throws InterruptedException {
        while (open && complete.isEmpty()) {
            wait();
        }
        
        if (!open && complete.isEmpty()) {
            throw new NoSuchElementException("closed");
        }
        
        return complete.remove(0);
    }

    /**
     * 
     */
    public synchronized AsyncFuture<V> poll() {
        if (!complete.isEmpty()) {
            return complete.remove(0);
        }
        return null;
    }

    /**
     * 
     */
    public synchronized AsyncFuture<V> poll(long timeout, TimeUnit unit) 
            throws InterruptedException {
        if (complete.isEmpty()) {
            unit.timedWait(this, timeout);
        }
        
        return poll();
    }

    @Override
    public void addAsyncFutureListener(AsyncFutureListener<V> listener) {
        listeners.add(Arguments.notNull(listener, "listener"));
    }

    @Override
    public void removeAsyncFutureListener(AsyncFutureListener<V> listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public AsyncFutureListener<V>[] getAsyncFutureListeners() {
        return listeners.toArray(new AsyncFutureListener[0]);
    }
}
