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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ardverk.concurrent.AsyncProcess.Delay;

/**
 * A cancellable asynchronous computation. This class provides a base
 * implementation of {@link AsyncFuture}, with methods to start and 
 * cancel a computation, query to see if the computation is complete, 
 * and retrieve the result of the computation.
 * 
 * @see AsyncFutureTask
 */
public class AsyncProcessFutureTask<V> extends AsyncFutureTask<V> 
        implements AsyncProcessRunnableFuture<V> {

    private static final ScheduledThreadPoolExecutor EXECUTOR 
        = ExecutorUtils.newSingleThreadScheduledExecutor("WatchdogThread");
    
    private static final AsyncProcess<Object> DEFAULT 
            = new AsyncProcess<Object>() {        
        @Override
        public void start(AsyncProcessFuture<Object> future) {
            throw new IllegalStateException();
        }
    };
    
    private final AsyncProcess<V> process;
    
    private final long timeout;
    
    private final TimeUnit unit;
    
    private ScheduledFuture<?> future = null;
    
    private boolean wasTimeout = false;
    
    /**
     * Creates an {@link AsyncProcessFutureTask}
     */
    @SuppressWarnings("unchecked")
    public AsyncProcessFutureTask() {
        this((AsyncProcess<V>)DEFAULT);
    }
    
    /**
     * Creates an {@link AsyncProcessFutureTask}
     */
    public AsyncProcessFutureTask(AsyncProcess<V> process) {
        this(process, -1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates an {@link AsyncProcessFutureTask}
     */
    public AsyncProcessFutureTask(AsyncProcess<V> process, 
            long timeout, TimeUnit unit) {
        this.process = process;
        this.timeout = timeout;
        this.unit = unit;
    }
    
    @Override
    protected void doRun() throws Exception {
        if (!isDone()) {
            watchdog(timeout, unit);
            start();
        }
    }
    
    /**
     * Starts the {@link AsyncProcess}.
     */
    protected void start() throws Exception {
        process.start(this);
    }
    
    private synchronized boolean watchdog(long timeout, TimeUnit unit) {
        if (timeout < 0 || isDone()) {
            return false;
        }
        
        Runnable task = new Runnable() {
            
            private final long creationTime = System.currentTimeMillis();
            
            @Override
            public void run() {
                synchronized (AsyncProcessFutureTask.this) {
                    if (!isDone() && !isDelay()) {
                        wasTimeout = true;
                        
                        long time = System.currentTimeMillis() - creationTime;
                        handleTimeout(time, TimeUnit.MILLISECONDS);
                    }
                }
            }
        };
        
        future = EXECUTOR.schedule(task, timeout, unit);
        return true;
    }
    
    /**
     * Returns true if the watchdog should be delayed.
     */
    private boolean isDelay() {
        long delay = getDelay(unit);
        return watchdog(delay, unit);
    }
    
    /**
     * You may override this method for custom delay implementations.
     */
    protected long getDelay(TimeUnit unit) {
        if (process instanceof Delay) {
            return ((Delay)process).getDelay(unit);
        }
        return -1;
    }
    
    /**
     * Called by the watchdog when a timeout occurred. The default
     * implementation will simply call {@link #setException(Throwable)}
     * with a {@link TimeoutException}.
     */
    protected synchronized boolean handleTimeout(long time, TimeUnit unit) {
        return setException(new TimeoutException("Watchdog: " + time + " " + unit));
    }
    
    @Override
    public long getTimeout(TimeUnit unit) {
        return unit.convert(timeout, this.unit);
    }
    
    @Override
    public long getTimeoutInMillis() {
        return getTimeout(TimeUnit.MILLISECONDS); 
    }
    
    @Override
    public synchronized boolean isTimeout() {
        return wasTimeout;
    }

    @Override
    protected final void done() {
        synchronized (this) {
            if (future != null) {
                future.cancel(true);
            }
        }
        
        super.done();
        done0();
    }
    
    /**
     * Called by {@link #done()}. Please read the documentation 
     * of {@link #done()} for further information.
     */
    protected void done0() {
        
    }
}
