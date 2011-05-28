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

package org.ardverk.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.ExecutorUtils;

/**
 * 
 */
public class IdleInputStream extends ProgressInputStream {

    private static final ScheduledExecutorService EXECUTOR 
        = ExecutorUtils.newSingleThreadScheduledExecutor("IdleInputStreamThread");
    
    private static final IdleCallback DEFAULT = new IdleAdapter();
    
    private volatile long timeStamp = System.currentTimeMillis();
    
    private final ScheduledFuture<?> future;
    
    public IdleInputStream(InputStream in, 
            long initialDelay, long delay, TimeUnit unit) {
        this(in, DEFAULT, initialDelay, delay, unit);
    }
    
    public IdleInputStream(InputStream in, IdleCallback callback, 
            long initialDelay, final long delay, final TimeUnit unit) {
        super(in, callback);
        
        Runnable task = new Runnable() {
            
            private final long timeoutInMillis = unit.toMillis(delay);
            
            @Override
            public void run() {
                long time = System.currentTimeMillis() - timeStamp;
                if (time >= timeoutInMillis) {
                    idle(time, TimeUnit.MILLISECONDS);
                }
            }
        };
        
        this.future = EXECUTOR.scheduleWithFixedDelay(
                task, initialDelay, delay, unit);
    }

    @Override
    public void close() throws IOException {
        future.cancel(true);
        super.close();
    }

    protected void idle(long time, TimeUnit unit) {
        ((IdleCallback)callback).idle(this, time, unit);
    }
    
    @Override
    public void in(int count) {
        timeStamp = System.currentTimeMillis();
        super.in(count);
    }
    
    public static interface IdleCallback extends ProgressCallback {
     
        /**
         * Called by {@link IdleInputStream} it 
         */
        public void idle(InputStream in, long time, TimeUnit unit);
    }
    
    public static class IdleAdapter extends ProgressAdapter 
            implements IdleCallback {

        @Override
        public void idle(InputStream in, long time, TimeUnit unit) {
        }
    }
}
