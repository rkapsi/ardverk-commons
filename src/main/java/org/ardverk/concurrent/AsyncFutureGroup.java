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
public class AsyncFutureGroup extends AbstractExecutorQueue<AsyncRunnableFuture<?>> {
    
    private final AsyncFutureListener<Object> listener
            = new AsyncFutureListener<Object>() {    
        @Override
        public void operationComplete(AsyncFuture<Object> future) {
            processNext(true);
        }
    };
    
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
        super(executor, queue);
        this.concurrencyLevel = Arguments.greaterZero(
                concurrencyLevel, "concurrencyLevel");
    }
    
    @Override
    public synchronized boolean isShutdown() {
        return !open;
    }
    
    @Override
    public synchronized boolean isTerminated() {
        return !open && queue.isEmpty();
    }
    
    @Override
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
    
    @Override
    public synchronized void shutdown() {
        if (open) {
            open = false;
            
            if (isTerminated()) {
                notifyAll();
            }
        }
    }
    
    @Override
    public synchronized List<AsyncRunnableFuture<?>> shutdownNow() {
        List<AsyncRunnableFuture<?>> copy 
            = new ArrayList<AsyncRunnableFuture<?>>(queue);
        queue.clear();
    
        shutdown();
        return copy;
    }
    
    @Override
    public synchronized void execute(AsyncRunnableFuture<?> future) 
            throws RejectedExecutionException {
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
                future.addAsyncFutureListener(listener);
                executor.execute(future);
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
