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

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ManagedScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor 
        implements ManagedExecutor {

    private final ScheduledFuture<?> future;
    
    public ManagedScheduledThreadPoolExecutor(int corePoolSize,
            RejectedExecutionHandler handler,
            long frequency, TimeUnit unit) {
        super(corePoolSize, handler);
        this.future = createPurgeTask(frequency, unit);
    }

    public ManagedScheduledThreadPoolExecutor(int corePoolSize,
            ThreadFactory threadFactory, RejectedExecutionHandler handler,
            long frequency, TimeUnit unit) {
        super(corePoolSize, threadFactory, handler);
        this.future = createPurgeTask(frequency, unit);
    }

    public ManagedScheduledThreadPoolExecutor(int corePoolSize,
            ThreadFactory threadFactory,
            long frequency, TimeUnit unit) {
        super(corePoolSize, threadFactory);
        this.future = createPurgeTask(frequency, unit);
    }

    public ManagedScheduledThreadPoolExecutor(int corePoolSize, 
            long frequency, TimeUnit unit) {
        super(corePoolSize);
        this.future = createPurgeTask(frequency, unit);
    }
    
    private ScheduledFuture<?> createPurgeTask(long frequency, TimeUnit unit) {
        if (frequency != -1L) {
            
            Runnable task = new ManagedRunnable() {
                @Override
                protected void doRun() {
                    purge();
                }
            };
            
            return scheduleWithFixedDelay(
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
