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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of {@link ThreadPoolExecutor} for {@link AsyncFuture}s.
 */
public class AsyncThreadPoolExecutor extends ThreadPoolExecutor 
        implements AsyncExecutorService {

    private volatile Time timeout = Time.NONE;
    
    public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, handler);
    }

    public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory);
    }

    public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    
    @Override
    public void setTimeout(long timeout, TimeUnit unit) {
        this.timeout = new Time(timeout, unit);
    }
    
    @Override
    public long getTimeout(TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        
        Time timeout = this.timeout;
        long value = timeout.getTime();
        if (value == -1L) {
            return -1L;
        }
        
        return unit.convert(value, timeout.getUnit());
    }
    
    protected <T> AsyncRunnableFuture<T> newTaskFor(AsyncProcess<T> process, 
            long timeout, TimeUnit unit) {
        return new AsyncFutureTask<T>(process, timeout, unit);
    }
    
    @Override
    public <T> AsyncFuture<T> submit(AsyncProcess<T> process) {
        return submit(process, timeout.getTime(), timeout.getUnit());
    }
    
    @Override
    public <T> AsyncFuture<T> submit(AsyncProcess<T> process, 
            long timeout, TimeUnit unit) {
        AsyncRunnableFuture<T> future = newTaskFor(process, timeout, unit);
        execute(future);
        return future;
    }
}
