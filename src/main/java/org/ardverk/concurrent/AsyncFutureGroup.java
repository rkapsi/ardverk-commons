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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.ardverk.lang.Arguments;

/**
 * An {@link AsyncFutureGroup} is a special purpose {@link Executor} 
 * for {@link AsyncRunnableFuture}s.
 */
public class AsyncFutureGroup {

    private final AsyncFutureListener<Object> listener
            = new AsyncFutureListener<Object>() {    
        @Override
        public void operationComplete(AsyncFuture<Object> future) {
            processNext(true);
        }
    };
    
    private final Executor executor;

    private final Queue<AsyncRunnableFuture<?>> queue;
    
    private final int concurrencyLevel;
    
    private int active = 0;
    
    /**
     * Creates an {@link AsyncFutureGroup}.
     */
    public AsyncFutureGroup(Executor executor, int concurrencyLevel) {
        this(executor, new LinkedList<AsyncRunnableFuture<?>>(), 
                concurrencyLevel);
    }
    
    /**
     * Creates an {@link AsyncFutureGroup}.
     */
    public AsyncFutureGroup(Executor executor, 
            Queue<AsyncRunnableFuture<?>> queue, int concurrencyLevel) {
        this.executor = Arguments.notNull(executor, "executor");
        this.queue = Arguments.notNull(queue, "queue");
        this.concurrencyLevel = Arguments.greaterZero(
                concurrencyLevel, "concurrencyLevel");
    }
    
    /**
     * Returns the number of elements in the {@link AsyncFutureGroup}
     */
    public synchronized int size() {
        return queue.size();
    }
    
    /**
     * Returns true if the {@link AsyncFutureGroup} is empty.
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * Cancels all {@link AsyncRunnableFuture}s in the queue.
     */
    public AsyncRunnableFuture<?>[] cancelAll(boolean mayInterruptIfRunning) {
        List<AsyncRunnableFuture<?>> copy = null;
        synchronized (this) {
            copy = new ArrayList<AsyncRunnableFuture<?>>(queue);
            queue.clear();
        }
        
        for (AsyncRunnableFuture<?> future : copy) {
            future.cancel(mayInterruptIfRunning);
        }
        
        return copy.toArray(new AsyncRunnableFuture[0]);
    }
    
    /**
     * Executes the given {@link AsyncRunnableFuture}.
     */
    public synchronized void execute(AsyncRunnableFuture<?> future) {
        boolean success = queue.offer(Arguments.notNull(future, "future"));
        if (!success) {
            throw new RejectedExecutionException();
        }
        
        processNext(false);
    }
    
    /**
     * Executes the next {@link AsyncRunnableFuture} from the queue.
     */
    @SuppressWarnings("unchecked")
    private synchronized void processNext(boolean callback) {
        if (callback) {
            assert (0 < active);
            --active;
        }
        
        if (active < concurrencyLevel && !queue.isEmpty()) {
            AsyncRunnableFuture<?> future = queue.poll();
            
            try {
                executor.execute(future);
                ((AsyncRunnableFuture<Object>)future)
                    .addAsyncFutureListener(listener);
                ++active;
            } catch (RejectedExecutionException err) {
                // Let everyone know there was an Exception!
                do {
                    future.setException(err);
                } while ((future = queue.poll()) != null);
            }
        }
    }
}
