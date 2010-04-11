package org.ardverk.concurrent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncProcessFutureTask<V> extends AsyncValueFuture<V> 
        implements AsyncRunnableFuture<V> {

    private static final ScheduledThreadPoolExecutor EXECUTOR 
        = ExecutorUtils.newSingleThreadScheduledExecutor("WatchdogThread");
    
    private static final AsyncProcess<Object> DEFAULT 
            = new AsyncProcess<Object>() {        
        @Override
        public void start(AsyncFuture<Object> future) {
            throw new IllegalStateException();
        }
    };
    
    private final AtomicReference<Interruptible> thread 
        = new AtomicReference<Interruptible>(Interruptible.INIT);
    
    private final AsyncProcess<V> process;
    
    private final long timeout;
    
    private final TimeUnit unit;
    
    private ScheduledFuture<?> future = null;
    
    private boolean wasTimeout = false;
    
    @SuppressWarnings("unchecked")
    public AsyncProcessFutureTask() {
        this((AsyncProcess<V>)DEFAULT, -1L, TimeUnit.MILLISECONDS);
    }
    
    public AsyncProcessFutureTask(AsyncProcess<V> process, 
            long timeout, TimeUnit unit) {
        this.process = process;
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public void run() {
        if (thread.compareAndSet(Interruptible.INIT, new CurrentThread())) {
            try {
                synchronized (this) {
                    if (!isDone()) {
                        try {
                            start();
                            watchdog();
                        } catch (Exception err) {
                            setException(err);
                        }
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
    
    protected synchronized void start() throws Exception {
        process.start(this);
    }
    
    private synchronized void watchdog() {
        if (timeout == -1L) {
            return;
        }
        
        if (isDone()) {
            return;
        }
        
        Runnable task = new Runnable() {
            @Override
            public void run() {
                synchronized (AsyncProcessFutureTask.this) {
                    if (!isDone()) {
                        wasTimeout = true;
                        setException(new TimeoutException());
                    }
                }
            }
        };
        
        future = EXECUTOR.schedule(task, timeout, unit);
    }
    
    public long getTimeout(TimeUnit unit) {
        return unit.convert(timeout, this.unit);
    }
    
    public long getTimeoutInMillis() {
        return getTimeout(TimeUnit.MILLISECONDS); 
    }
    
    public synchronized boolean isTimeout() {
        return wasTimeout;
    }

    @Override
    protected void done() {
        synchronized (this) {
            if (future != null) {
                future.cancel(true);
            }
        }
        
        super.done();
    }
}
