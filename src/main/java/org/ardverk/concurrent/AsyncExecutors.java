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

import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.ardverk.utils.EventThreadProvider;

/**
 * Factory and utility methods for {@link AsyncExecutor}, 
 * {@link AsyncExecutorService}, {@link ThreadFactory}, 
 * {@link AsyncProcess}, {@link Runnable} and {@link Callable} 
 * classes defined in this package.
 * 
 * This class is also responsible for event dispatching through
 * the {@link EventThreadProvider} interface. Custom implementations 
 * may be loaded through the {@link ServiceLoader} facilities.
 * 
 * @see Executors
 * @see ServiceLoader
 * @see EventThreadProvider
 */
public class AsyncExecutors {
    
    private AsyncExecutors() {}
    
    /**
     * Creates and returns a cached {@link Thread} pool with no timeout.
     * 
     * @see Executors#newCachedThreadPool()
     */
    public static AsyncExecutorService newCachedThreadPool() {
        return newCachedThreadPool(-1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a cached {@link Thread} pool with the given
     * timeout.
     * 
     * @see Executors#newCachedThreadPool()
     */
    public static AsyncExecutorService newCachedThreadPool(
            long timeout, TimeUnit unit) {
        AsyncThreadPoolExecutor executor = new AsyncThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                -1L, TimeUnit.MILLISECONDS);
        executor.setTimeout(timeout, unit);
        return executor;
    }
    
    /**
     * Creates and returns a cached {@link Thread} pool with no timeout.
     * 
     * @see Executors#newCachedThreadPool(ThreadFactory)
     */
    public static AsyncExecutorService newCachedThreadPool(
            ThreadFactory threadFactory) {
        return newCachedThreadPool(threadFactory, -1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a cached {@link Thread} pool with the given
     * timeout.
     * 
     * @see Executors#newCachedThreadPool(ThreadFactory)
     */
    public static AsyncExecutorService newCachedThreadPool(
            ThreadFactory threadFactory, long timeout, TimeUnit unit) {
        AsyncThreadPoolExecutor executor = new AsyncThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory,
                -1L, TimeUnit.MILLISECONDS);
        executor.setTimeout(timeout, unit);
        return executor;
    }
    
    /**
     * Creates and returns a single {@link Thread} executor with no timeout.
     * 
     * @see Executors#newSingleThreadExecutor()
     */
    public static AsyncExecutorService newSingleThreadExecutor() {
        return newSingleThreadExecutor(-1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a single {@link Thread} executor with the given
     * timeout.
     * 
     * @see Executors#newSingleThreadExecutor()
     */
    public static AsyncExecutorService newSingleThreadExecutor(
            long timeout, TimeUnit unit) {
        AsyncThreadPoolExecutor executor = new AsyncThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                -1L, TimeUnit.MILLISECONDS);
        executor.setTimeout(timeout, unit);
        return executor;
    }
    
    /**
     * Creates and returns a single {@link Thread} executor with no timeout.
     * 
     * @see Executors#newSingleThreadExecutor(ThreadFactory)
     */
    public static AsyncExecutorService newSingleThreadExecutor(
            ThreadFactory threadFactory) {
        return newSingleThreadExecutor(threadFactory, -1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a single {@link Thread} executor with the given
     * timeout.
     * 
     * @see Executors#newSingleThreadExecutor(ThreadFactory)
     */
    public static AsyncExecutorService newSingleThreadExecutor(
            ThreadFactory threadFactory, long timeout, TimeUnit unit) {
        AsyncThreadPoolExecutor executor = new AsyncThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                -1L, TimeUnit.MILLISECONDS);
        executor.setTimeout(timeout, unit);
        return executor;
    }
    
    /**
     * Creates and returns a fixed size {@link Thread} pool with no timeout.
     * 
     * @see Executors#newFixedThreadPool(int)
     */
    public static AsyncExecutorService newFixedThreadPool(int nThreads) {
        return newFixedThreadPool(nThreads, -1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a fixed size {@link Thread} executor with the given
     * timeout.
     * 
     * @see Executors#newFixedThreadPool(int)
     */
    public static AsyncExecutorService newFixedThreadPool(int nThreads, 
            long timeout, TimeUnit unit) {
        AsyncThreadPoolExecutor executor = new AsyncThreadPoolExecutor(
                nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                -1L, TimeUnit.MILLISECONDS);
        executor.setTimeout(timeout, unit);
        return executor;
    }
    
    /**
     * Creates and returns a fixed size {@link Thread} pool with no timeout.
     * 
     * @see Executors#newFixedThreadPool(int, ThreadFactory)
     */
    public static AsyncExecutorService newFixedThreadPool(
            int nThreads, ThreadFactory threadFactory) {
        return newFixedThreadPool(nThreads, threadFactory, 
                -1L, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns a fixed size {@link Thread} executor with the given
     * timeout.
     * 
     * @see Executors#newFixedThreadPool(int, ThreadFactory)
     */
    public static AsyncExecutorService newFixedThreadPool(int nThreads, 
            ThreadFactory threadFactory, long timeout, TimeUnit unit) {
        AsyncThreadPoolExecutor executor = new AsyncThreadPoolExecutor(
                nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), threadFactory,
                -1L, TimeUnit.MILLISECONDS);
        executor.setTimeout(timeout, unit);
        return executor;
    }

    /**
     * Creates and returns an {@link AsyncProcess} for the 
     * given {@link Runnable}.
     */
    public static AsyncProcess<?> process(Runnable task, 
            AsyncFutureListener<?>... listeners) {
        return process(Executors.callable(task));
    }
    
    /**
     * Creates and returns an {@link AsyncProcess} for the 
     * given {@link Runnable} and value.
     */
    public static <V> AsyncProcess<V> process(Runnable task, V value, 
            AsyncFutureListener<V>... listeners) {
        return process(Executors.callable(task, value));
    }
    
    /**
     * Creates and returns an {@link AsyncProcess} for the 
     * given {@link Callable}.
     */
    public static <V> AsyncProcess<V> process(final Callable<V> task, 
            final AsyncFutureListener<V>... listeners) {
        
        if (task == null) {
            throw new NullPointerException("task");
        }
        
        if (listeners == null) {
            throw new NullPointerException("listeners");
        }
        
        return new AsyncProcess<V>() {
            @Override
            public void start(AsyncFuture<V> future) throws Exception {
                
                for (AsyncFutureListener<V> l : listeners) {
                    future.addAsyncFutureListener(l);
                }
                
                future.setValue(task.call());
            }
        };
    }
}
