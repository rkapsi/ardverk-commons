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

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.ardverk.lang.Arguments;
import org.ardverk.lang.NullArgumentException;

/**
 * An {@link AsyncFutureGroup} is a special purpose {@link Executor} 
 * for {@link AsyncRunnableFuture}s. 
 * 
 * The idea is very similar to {@link ExecutorGroup} where an external
 * {@link Executor} is being used and the group is managing how many
 * queued tasks are running in parallel.
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
    
    private boolean open = true;
    
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
     * Returns the {@link AsyncFutureGroup}'s {@link Executor}.
     */
    public Executor getExecutor() {
        return executor;
    }
    
    /**
     * Returns the {@link AsyncFutureGroup}'s {@link Queue}.
     */
    public Queue<AsyncRunnableFuture<?>> getQueue() {
        return queue;
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
     * Returns true if the {@link AsyncFutureGroup} is shutdown.
     */
    public synchronized boolean isShutdown() {
        return !open;
    }
    
    /**
     * Returns true if the {@link AsyncFutureGroup} is terminated.
     */
    public synchronized boolean isTerminated() {
        return !open && queue.isEmpty();
    }
    
    /**
     * Waits for the {@link AsyncFutureGroup} to terminate. Returns
     * true if the {@link AsyncFutureGroup} is terminated and false
     * if it isn't.
     */
    public synchronized boolean awaitTermination(long timeout, TimeUnit unit) 
            throws InterruptedException {
        
        if (timeout < 0L) {
            throw new IllegalArgumentException("timeout=" + timeout);
        }
        
        if (unit == null) {
            throw new NullArgumentException("unit");
        }
        
        if (!isTerminated()) {
            if (timeout == 0L) {
                wait();
            } else {
                unit.timedWait(this, timeout);
            }
        }
        
        return isTerminated();
    }
    
    /**
     * Shuts down the {@link AsyncFutureGroup}. Queued tasks
     * continue to be executed but no new tasks will be accepted.
     */
    public synchronized void shutdown() {
        if (open) {
            open = false;
            
            if (isTerminated()) {
                notifyAll();
            }
        }
    }
    
    /**
     * Shuts down the {@link AsyncFutureGroup} and returns all 
     * {@link AsyncRunnableFuture}s that were in the queue.
     */
    public synchronized AsyncRunnableFuture<?>[] shutdownNow() {
        AsyncRunnableFuture<?>[] copy 
            = queue.toArray(new AsyncRunnableFuture[0]);
        queue.clear();
    
        shutdown();
        return copy;
    }
    
    /**
     * Submits the given {@link AsyncRunnableFuture} for execution.
     */
    public synchronized void submit(AsyncRunnableFuture<?> future) {
        if (!open) {
            throw new RejectedExecutionException();
        }
        
        boolean success = queue.offer(Arguments.notNull(future, "future"));
        if (!success) {
            throw new RejectedExecutionException();
        }
        
        processNext(false);
    }
    
    /**
     * Executes the next {@link AsyncRunnableFuture} from the queue.
     */
    private synchronized void processNext(boolean callback) {
        if (callback) {
            assert (0 < active);
            --active;
        }
        
        while (active < concurrencyLevel) {
            @SuppressWarnings("unchecked")
            AsyncRunnableFuture<Object> future 
                = (AsyncRunnableFuture<Object>)queue.poll();
            if (future == null) {
                break;
            }
            
            try {
                executor.execute(future);
                future.addAsyncFutureListener(listener);
                ++active;
            } catch (RejectedExecutionException err) {
                handleRejection(future, err);
            }
        }
        
        if (queue.isEmpty()) {
            notifyAll();
        }
    }
    
    // Awwww
    private synchronized void handleRejection(
            AsyncFuture<?> future, RejectedExecutionException err) {
        
        // Let the future know there was an Exception, drain the 
        // queue and do the same for the remaining Futures because
        // the same will happen to them.
        do {
            future.setException(err);
        } while ((future = queue.poll()) != null);
    }
}
