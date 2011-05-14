/*
 * Copyright 2010-2011 Roger Kapsi
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

package org.ardverk.utils;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.EventUtils;
import org.ardverk.concurrent.ExecutorUtils;
import org.ardverk.concurrent.FutureUtils;

public class Idle implements Closeable {

    private static final ScheduledExecutorService EXECUTOR 
        = ExecutorUtils.newSingleThreadScheduledExecutor("IdleThread");
    
    private final Map<IdleListener, ScheduledFuture<?>> listeners 
        = new ConcurrentHashMap<Idle.IdleListener, ScheduledFuture<?>>();
    
    private volatile long timeStamp = System.currentTimeMillis();
    
    private final long frequencyInMillis;
    
    public Idle(long frequency, TimeUnit unit) {
        this.frequencyInMillis = unit.toMillis(frequency);
    }
    
    public void touch() {
        timeStamp = System.currentTimeMillis();
    }
    
    @Override
    public void close() {
        FutureUtils.cancelAll(listeners.values(), true);
    }

    public synchronized void addIdleListener(final IdleListener l, 
            final long timeout, final TimeUnit unit) {
        
        Runnable task = new Runnable() {
            
            private final long timeoutInMillis = unit.toMillis(timeout);
            
            @Override
            public void run() {
                long time = System.currentTimeMillis() - timeStamp;
                if (time >= timeoutInMillis) {
                    timeout(l, timeout, unit);
                }
            }
        };
        
        ScheduledFuture<?> future = EXECUTOR.scheduleWithFixedDelay(
                task, frequencyInMillis, frequencyInMillis, 
                TimeUnit.MILLISECONDS);
        
        ScheduledFuture<?> existing = listeners.put(l, future);
        if (existing != null) {
            existing.cancel(true);
        }
    }
    
    public synchronized void removeIdleListener(IdleListener l) {
        ScheduledFuture<?> future = listeners.remove(l);
        if (future != null) {
            future.cancel(true);
        }
    }
    
    private synchronized void timeout(IdleListener l, 
            long timeout, TimeUnit unit) {
        
        ScheduledFuture<?> future = listeners.get(l);
        if (future != null) {
            future.cancel(true);
        }
        
        fireIdleEvent(l, timeout, unit);
    }
    
    protected void fireIdleEvent(final IdleListener l, 
            final long timeout, final TimeUnit unit) {
        
        if (!listeners.isEmpty()) {
            Runnable event = new Runnable() {
                @Override
                public void run() {
                    for (IdleListener l : listeners.keySet()) {
                        l.handleIdle(l, timeout, unit);
                    }
                }
            };
            EventUtils.fireEvent(event);
        }
    }
    
    public static interface IdleListener {
        public void handleIdle(IdleListener l, long timeout, TimeUnit unit);
    }
}
