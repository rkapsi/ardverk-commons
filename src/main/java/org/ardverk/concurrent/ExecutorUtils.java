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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ardverk.utils.SystemUtils;

/**
 * A utility class to create {@link ScheduledThreadPoolExecutor}s and
 * {@link ThreadPoolExecutor}s.
 */
public class ExecutorUtils {

    private static final long PURGE_FREQUENCY = SystemUtils.getLong(
            ExecutorUtils.class, "purgeFrequency", 30L * 1000L);
    
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
        
        return new ManagedScheduledThreadPoolExecutor(corePoolSize, 
                defaultThreadFactory(name), frequency, unit);
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
        
        return new ManagedThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), 
                defaultThreadFactory(name),
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
        
        return new ManagedThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), 
                defaultThreadFactory(name),
                frequency, unit);
    }

    /**
     * Creates and returns a {@link ThreadFactory} which creates 
     * {@link Thread}s that are pre-fixed with the given name.
     */
    public static ThreadFactory defaultThreadFactory(String name) {
        return new DefaultThreadFactory(name);
    }
}
