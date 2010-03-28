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
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ManagedThreadPoolExecutor extends ThreadPoolExecutor 
        implements ManagedExecutor {

    private static final ScheduledThreadPoolExecutor EXECUTOR 
        = ExecutorUtils.newSingleThreadScheduledExecutor(
            "ManagedThreadPoolExecutorThread");
    
    private final ScheduledFuture<?> future;
    
    public ManagedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler,
            long purgeFrequency, TimeUnit purgeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.future = createPurgeTask(purgeFrequency, purgeUnit);
    }

    public ManagedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
            RejectedExecutionHandler handler,
            long purgeFrequency, TimeUnit purgeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, 
                workQueue, threadFactory, handler);
        this.future = createPurgeTask(purgeFrequency, purgeUnit);
    }
    
    public ManagedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            long purgeFrequency, TimeUnit purgeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.future = createPurgeTask(purgeFrequency, purgeUnit);
    }

    public ManagedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            long purgeFrequency, TimeUnit purgeUnit) {
        super(corePoolSize, maximumPoolSize, 
                keepAliveTime, unit, workQueue, threadFactory);    
        this.future = createPurgeTask(purgeFrequency, purgeUnit);
    }

    private ScheduledFuture<?> createPurgeTask(long frequency, TimeUnit unit) {
        if (frequency != -1L) {
            
            Runnable task = new ManagedRunnable() {
                @Override
                protected void doRun() {
                    purge();
                }
            };
            
            return EXECUTOR.scheduleWithFixedDelay(
                    task, frequency, frequency, unit);
        }
        return null;
    }
    
    @Override
    protected void terminated() {
        if (future != null) {
            future.cancel(true);
        }
        
        super.terminated();
    }
}