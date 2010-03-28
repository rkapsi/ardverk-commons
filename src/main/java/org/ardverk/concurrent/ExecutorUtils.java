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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A utility class to create {@link ScheduledThreadPoolExecutor}s and
 * {@link ThreadPoolExecutor}s.
 */
public class ExecutorUtils {

    private static final long PURGE_FREQUENCY = 30L * 1000L;
    
    private ExecutorUtils() {}
    
    /**
     * Creates and returns a {@link ScheduledThreadPoolExecutor}
     */
    public static ScheduledThreadPoolExecutor newSingleThreadScheduledExecutor(String name) {
        return newSingleThreadScheduledExecutor(name, PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a {@link ScheduledThreadPoolExecutor}
     */
    public static ScheduledThreadPoolExecutor newSingleThreadScheduledExecutor(
            String name, long frequency, TimeUnit unit) {
        return newScheduledThreadPool(1, name, frequency, unit);
    }
    
    /**
     * Creates and returns a {@link ScheduledThreadPoolExecutor}
     */
    public static ScheduledThreadPoolExecutor newScheduledThreadPool(
            int corePoolSize, String name) {
        return newScheduledThreadPool(corePoolSize, name, PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a {@link ScheduledThreadPoolExecutor}
     */
    public static ScheduledThreadPoolExecutor newScheduledThreadPool(
            int corePoolSize, String name, long frequency, TimeUnit unit) {
        
        ThreadFactory threadFactory 
            = new DefaultThreadFactory(name);
        
        final ScheduledThreadPoolExecutor executor 
            = new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
        
        Runnable task = new ManagedRunnable() {
            @Override
            protected void doRun() {
                executor.purge();
            }
        };
        
        executor.scheduleWithFixedDelay(task, frequency, frequency, unit);
        return executor;
    }

    /**
     * Creates and returns a {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newCachedThreadPool(String name) {
        return newCachedThreadPool(name, PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newCachedThreadPool(String name, 
            long frequency, TimeUnit unit) {
        
        ThreadFactory threadFactory 
            = new DefaultThreadFactory(name);
            
        return new ManagedExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), 
                threadFactory,
                frequency, unit);
    }
    
    /**
     * Creates and returns a {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newSingleThreadExecutor(int nThreads, String name) {
        return newSingleThreadExecutor(nThreads, name, PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newSingleThreadExecutor(int nThreads, 
            String name, long frequency, TimeUnit unit) {
        return newFixedThreadPool(1, name, frequency, unit);
    }
    
    /**
     * Creates and returns a {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, String name) {
        return newFixedThreadPool(nThreads, name, PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, 
            String name, long frequency, TimeUnit unit) {
        
        ThreadFactory threadFactory 
            = new DefaultThreadFactory(name);
            
        return new ManagedExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), 
                threadFactory,
                frequency, unit);
    }
    
    private static class ManagedExecutor extends ThreadPoolExecutor {

        private static final ScheduledThreadPoolExecutor EXECUTOR 
            = ExecutorUtils.newSingleThreadScheduledExecutor(
                "ManagedExecutorThread");
        
        private final ScheduledFuture<?> future;
        
        public ManagedExecutor(int corePoolSize, int maximumPoolSize,
                long keepAliveTime, TimeUnit unit,
                BlockingQueue<Runnable> workQueue,
                ThreadFactory threadFactory,
                long purgeFrequency, TimeUnit purgeUnit) {
            super(corePoolSize, maximumPoolSize, 
                    keepAliveTime, unit, workQueue, threadFactory);
            
            Runnable task = new ManagedRunnable() {
                @Override
                protected void doRun() {
                    ManagedExecutor.this.purge();
                }
            };
            
            ScheduledFuture<?> future = null;
            if (purgeFrequency >= 0L) {
                future = EXECUTOR.scheduleWithFixedDelay(
                        task, purgeFrequency, purgeFrequency, purgeUnit);
            }
            
            this.future = future;
        }

        @Override
        protected void terminated() {
            if (future != null) {
                future.cancel(true);
            }
            
            super.terminated();
        }
    }
}
