package org.ardverk.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.DefaultThreadFactory;
import org.ardverk.concurrent.ManagedRunnable;
import org.ardverk.lang.NullArgumentException;

/**
 * An utility class for Deadlock scanning.
 */
public class DeadlockScanner {
    
    private static final long FREQUENCY = 3L * 1000L;
    
    private static final Callback CALLBACK = new Callback() {
        @Override
        public void deadlock(Deadlock deadlock) {
            System.err.println(deadlock);
        }
    };
    
    private static final ScheduledExecutorService EXECUTOR 
        = Executors.newSingleThreadScheduledExecutor(
            new DefaultThreadFactory("DeadlockScannerThread", true));
    
    private static ScheduledFuture<?> FUTURE = null;
    
    /**
     * Starts the {@link DeadlockScanner}.
     */
    public static synchronized void start() {
        start(CALLBACK, FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Starts the {@link DeadlockScanner}.
     */
    public static synchronized void start(Callback callback) {
        start(callback, FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Starts the {@link DeadlockScanner}.
     */
    public static synchronized void start(long frequency, TimeUnit unit) {
        start(CALLBACK, frequency, unit);
    }
    
    /**
     * Starts the {@link DeadlockScanner}.
     */
    public static synchronized void start(final Callback callback, 
            long frequency, TimeUnit unit) {
        
        if (callback == null) {
            throw new NullArgumentException("callback");
        }
        
        if (frequency < 0L) {
            throw new IllegalArgumentException("frequency=" + frequency);
        }
        
        if (unit == null) {
            throw new NullArgumentException("unit");
        }
        
        if (FUTURE != null) {
            FUTURE.cancel(true);
        }
        
        Runnable task = new ManagedRunnable() {
            @Override
            protected void doRun() {
                checkForDeadlock(callback);
            }
        };
        
        FUTURE = EXECUTOR.scheduleWithFixedDelay(
                task, frequency, frequency, unit);
    }
    
    /**
     * Stops the {@link DeadlockScanner}.
     */
    public static synchronized void stop() {
        if (FUTURE != null) {
            FUTURE.cancel(true);
        }
    }
    
    /**
     * Returns true if the {@link DeadlockScanner} is running.
     */
    public synchronized boolean isRunning() {
        return FUTURE != null && !FUTURE.isDone();
    }
    
    private static void checkForDeadlock(Callback callback) {
        
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] deadlocks = bean.findDeadlockedThreads();
        
        if (deadlocks.length == 0) {
            return;
        }
        
        callback.deadlock(new Deadlock(deadlocks));
    }

    private DeadlockScanner() {}
    
    /**
     * 
     */
    public static interface Callback {
        
        /**
         * 
         */
        public void deadlock(Deadlock deadlock);
    }
    
    /**
     * 
     */
    public static class Deadlock {
        
        private final long[] deadlocks;
        
        private Deadlock(long[] deadlocks) {
            this.deadlocks = deadlocks;
        }
        
        public long[] getDeadlocks() {
            return deadlocks;
        }
        
        @Override
        public String toString() {
            return "Deadlock";
        }
    }
}
