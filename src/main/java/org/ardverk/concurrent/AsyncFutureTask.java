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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ardverk.utils.SystemUtils;

/**
 * A cancellable asynchronous computation. This class provides a base
 * implementation of {@link AsyncFuture}, with methods to start and 
 * cancel a computation, query to see if the computation is complete, 
 * and retrieve the result of the computation.
 * 
 * @see FutureTask
 */
public class AsyncFutureTask<V> implements AsyncRunnableFuture<V> {
    
    /**
     * The Watchdog {@link Thread}s
     */
    private static final ScheduledThreadPoolExecutor EXECUTOR 
        = createWatchdogExecutor(4, "AsyncFutureWatchdogThread");
    
    /**
     * An {@link AsyncProcess} that throws an {@link IllegalStateException}.
     */
    private static final AsyncProcess<Object> PROCESS 
            = new AsyncProcess<Object>() {
        @Override
        public void start(AsyncFuture<Object> future) {
            throw new IllegalStateException(
                    "Must override AsyncFutureTask.start()");
        }
    };
    
    /**
     * The list of {@link AsyncFutureListener}s that were added
     * before the {@link AsyncFuture} completed.
     */
    private List<AsyncFutureListener<V>> beforeListeners = null;
    
    /**
     * The list of {@link AsyncFutureListener}s that were added
     * after the {@link AsyncFuture} completed.
     */
    private List<AsyncFutureListener<V>> afterListeners = null;
    
    /**
     * The synchronization point that is being used to essentially
     * wire all ends together.
     */
    private final AsyncExchanger<V> exchanger 
        = new AsyncExchanger<V>(this);
    
    private final AsyncProcess<V> process;
    
    private final long timeout;
    
    private final TimeUnit unit;
    
    private boolean running = false;
    
    private ScheduledFuture<?> watchdog = null;
    
    private boolean wasTimeout = false;
    
    /**
     * Creates an {@link AsyncFutureTask} without an {@link AsyncProcess} 
     * and with no timeout.
     * 
     * NOTE: Subclasses must override the {@link #start()} method.
     */
    public AsyncFutureTask() {
        this (-1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates an {@link AsyncFutureTask} without an {@link AsyncProcess}
     * and the given timeout.
     * 
     * NOTE: Subclasses must override the {@link #start()} method.
     */
    @SuppressWarnings("unchecked")
    public AsyncFutureTask(long timeout, TimeUnit unit) {
        this ((AsyncProcess<V>)PROCESS, timeout, unit);
    }
    
    /**
     * Creates an {@link AsyncFutureTask} with the given {@link AsyncProcess}
     * and with no timeout.
     */
    public AsyncFutureTask(AsyncProcess<V> process) {
        this(process, -1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and {@link AsyncFutureTask} with the given {@link AsyncProcess}
     * and timeout.
     */
    public AsyncFutureTask(AsyncProcess<V> process, 
            long timeout, TimeUnit unit) {
        if (process == null) {
            throw new NullPointerException("processs");
        }
        
        if (timeout < 0L && timeout != -1L) {
            throw new IllegalArgumentException("timeout=" + timeout);
        }
        
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        
        this.process = process;
        this.timeout = timeout;
        this.unit = unit;
    }
    
    @Override
    public long getTimeout(TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        
        if (timeout == -1L) {
            return -1L;
        }
        
        return unit.convert(timeout, this.unit);
    }

    @Override
    public synchronized final void run() {
        if (running) {
            throw new IllegalStateException("Already running!");
        }
        
        running = true;
        
        try {
            if (!isDone()) {
                start();
                watchdog();
            }
        } catch (Throwable t) {
            setException(t);
        }
    }
    
    /**
     * Called by the {@link #run()} method. Subclasses may override
     * this method.
     * 
     * NOTE: This method is being called while a lock is being held
     * on <tt>this</tt> Object.
     */
    protected void start() throws Exception {
        process.start(this);
    }
    
    /**
     * Called by the {@link #run()} method to start the watchdog.
     * 
     * NOTE: This method is being called while a lock is being held
     * on <tt>this</tt> Object.
     */
    private synchronized boolean watchdog() {
        long timeout = this.timeout;
        
        // See if we can get a timeout from the AsyncProcess
        if (timeout <= 0L) {
            timeout = delay(unit);
        }
        
        return watchdog(timeout, unit);
    }
    
    private synchronized boolean watchdog(
            long timeout, final TimeUnit unit) {
        
        if (timeout <= 0L) {
            return false;
        }
        
        if (isDone()) {
            return false;
        }
        
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (AsyncFutureTask.this) {
                    try {
                        if (!isDone()) {
                            long delay = delay(unit);
                            if (0L < delay) {
                                watchdog(delay, unit);
                                return;
                            }
                            
                            wasTimeout = true;
                            watchdogWillKillFuture();
                            setException(new TimeoutException());
                        }
                    } catch (Throwable t) {
                        setException(t);
                    }
                }
            }
        };
        
        if (watchdog != null) {
            watchdog.cancel(true);
        }
        
        watchdog = EXECUTOR.schedule(task, timeout, unit);
        return true;
    }
    
    /**
     * Called by the watchdog {@link Thread}. Subclasses may override this
     * method.
     * 
     * NOTE: This method is being called while a lock is being held
     * on <tt>this</tt> Object.
     */
    protected long delay(TimeUnit unit) {
        if (process instanceof Delay) {
            ((Delay)process).delay(unit);
        }
        
        return -1L;
    }
    
    /**
     * This method is being called right before the watchdog is killing 
     * this {@link AsyncFuture}.
     * 
     * NOTE: This method is being called while a lock is being held
     * on <tt>this</tt> Object.
     */
    protected void watchdogWillKillFuture() {
        // OVERRIDE
    }
    
    @Override
    public void setValue(V value) {
        boolean complete = false;
        synchronized (this) {
            if (!exchanger.isDone()) {
                exchanger.setValue(value);
                complete = true;
            }
            
            if (watchdog != null) {
                watchdog.cancel(true);
                watchdog = null;
            }
        }
        
        if (complete) {
            complete();
        }
    }
    
    @Override
    public void setException(Throwable t) {
        if (t == null) {
            throw new NullPointerException("t");
        }
        
        boolean complete = false;
        synchronized (this) {
            if (!exchanger.isDone()) {
                exchanger.setException(new ExecutionException(t));
                complete = true;
            }
            
            if (watchdog != null) {
                watchdog.cancel(true);
                watchdog = null;
            }
        }
        
        if (complete) {
            complete();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancelled = false;
        boolean complete = false;
        synchronized (this) {
            cancelled = exchanger.isDone();
            if (!cancelled) {
                cancelled = exchanger.cancel();
                complete = true;
            }
        }
        
        if (complete) {
            complete();
        }
        
        return cancelled;
    }

    @Override
    public synchronized boolean isTimeout() {
        return isDone() && wasTimeout;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        checkIfEventThread();
        return exchanger.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        checkIfEventThread();
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

    @Override
    public V tryGet() throws InterruptedException, ExecutionException {
        return exchanger.tryGet();
    }

    private void complete() {
        done();
        fireOperationComplete();
    }
    
    /**
     * Called when the {@link AsyncFutureTask} is done (complete). 
     * Subclasses may override this method.
     */
    protected void done() {
        // OVERRIDE
    }
    
    @Override
    public synchronized void addAsyncFutureListener(final AsyncFutureListener<V> l) {
        if (l == null) {
            throw new NullPointerException("l");
        }
        
        // Add it to the listeners list if AsyncFuture is not done yet!
        if (!isDone()) {
            if (beforeListeners == null) {
                beforeListeners = new ArrayList<AsyncFutureListener<V>>();
            }
            
            beforeListeners.add(l);
            return;
        }
        
        // Add it to the late listeners list and fire an event right away.
        if (afterListeners == null) {
            afterListeners = new ArrayList<AsyncFutureListener<V>>();
        }
        
        afterListeners.add(l);
        
        Runnable event = new Runnable() {
            public void run() {
                l.operationComplete(AsyncFutureTask.this);
            }
        };
        
        AsyncExecutors.fireEvent(event);
    }

    @Override
    public synchronized void removeAsyncFutureListener(AsyncFutureListener<V> l) {
        if (l == null) {
            throw new NullPointerException("l");
        }
        
        boolean success = false;
        if (beforeListeners != null) {
            success = beforeListeners.remove(l);
        }
        
        if (!success && afterListeners != null) {
            afterListeners.remove(l);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public synchronized AsyncFutureListener<V>[] getAsyncFutureListeners() {
        List<AsyncFutureListener<V>> copy 
            = new ArrayList<AsyncFutureListener<V>>();
        if (beforeListeners != null) {
            copy.addAll(beforeListeners);
        }
        
        if (afterListeners != null) {
            copy.addAll(afterListeners);
        }
        
        return copy.toArray(new AsyncFutureListener[0]);
    }
    
    /**
     * Returns the {@link AsyncFutureListener}s that were added before
     * this {@link AsyncFuture} completed.
     */
    @SuppressWarnings("unchecked")
    private synchronized AsyncFutureListener<V>[] getBeforeListeners() {
        if (beforeListeners != null) {
            return beforeListeners.toArray(new AsyncFutureListener[0]);
        }
        return null;
    }
    
    /**
     * Notifies all {@link AsyncFutureListener}s
     * 
     * NOTE: Only {@link AsyncFutureListener}s that were added before
     * {@link AsyncFuture} completion will be notified.
     */
    protected void fireOperationComplete() {
        final AsyncFutureListener<V>[] listeners 
            = getBeforeListeners();
        
        if (listeners != null && listeners.length > 0) {
            Runnable event = new Runnable() {
                @Override
                public void run() {
                    for (AsyncFutureListener<V> l : listeners) {
                        l.operationComplete(AsyncFutureTask.this);
                    }
                }
            };
            
            AsyncExecutors.fireEvent(event);
        }
    }
    
    /**
     * Make sure that nobody can call any of the {@link #get()} methods 
     * from the event {@link Thread} as it is very difficult to debug.
     */
    protected void checkIfEventThread() throws IllegalStateException {
        if (AsyncExecutors.isEventThread() && !isDone()) {
            throw new IllegalStateException(
                    "Can not call get() from the EventThread!");
        }
    }
    
    @Override
    public String toString() {
        return exchanger.toString();
    }
    
    /**
     * A helper method to create the watchdog scheduler
     */
    private static ScheduledThreadPoolExecutor createWatchdogExecutor(
            int defaultThreadCount, String name) {
        int nThreads = SystemUtils.getInteger(
                AsyncFutureTask.class, "watchdogThreadCount", defaultThreadCount);
        return AsyncExecutors.newScheduledThreadPool(nThreads, name);
    }
}
