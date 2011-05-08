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

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.EventUtils;
import org.ardverk.concurrent.ExecutorUtils;
import org.ardverk.concurrent.FutureUtils;

/**
 * The {@link IdleInputStream} fires events if nothing have been read
 * for this {@link InputStream} for a given period of time. It's also
 * firing EOF and CLOSE events.
 */
public class IdleInputStream extends FilterInputStream {

    private static final ScheduledThreadPoolExecutor EXECUTOR 
        = ExecutorUtils.newSingleThreadScheduledExecutor(
                "IdleInputStreamThread");
    
    private static final IdleListener DEFAULT = new IdleAdapter() {
        @Override
        public void handleClosed(IdleInputStream in) {
        }
    };
    
    private final ConcurrentHashMap<IdleListener, ScheduledFuture<?>> listeners 
        = new ConcurrentHashMap<IdleListener, ScheduledFuture<?>>();
        
    private boolean open = true;
    
    private boolean eof = false;
    
    private volatile long timeStamp;
    
    public IdleInputStream(InputStream in) {
        super(in);
    }
    
    public boolean isOpen() {
        return open;
    }
    
    public boolean isEOF() {
        return eof;
    }
    
    private int init() throws IOException {
        if (eof && !open) {
            throw new EOFException();
        }
        
        if (!open) {
            throw new IOException();
        }
        
        return -1;
    }
    
    @Override
    public int read() throws IOException {
        int value = init();
        try {
            value = super.read();
        } finally {
            process(value);
        }
        return value;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int r = init();
        try {
            r = super.read(b, off, len);
        } finally {
            process(r);
        }
        return r;
    }

    private void process(int value) {
        if (value == -1) {
            if (!eof) {
                synchronized (listeners) {
                    eof = true;
                }
                
                handleEOF();
            }
        } else {
            timeStamp = System.currentTimeMillis();
        }
    }
    
    @Override
    public void close() throws IOException {   
        if (open) {
            
            synchronized (listeners) {
                open = false;
                FutureUtils.cancelAll(listeners.values(), true);
            }
            
            try {
                super.close();
            } finally {
                handleClosed();
            }
        }
    }
    
    public void addIdleListener(IdleListener l) {
        addIdleListener(l, -1L, TimeUnit.MILLISECONDS);
    }
    
    public void addIdleListener(long timeout, TimeUnit unit) {
        addIdleListener(DEFAULT, timeout, unit);
    }
    
    public void addIdleListener(final IdleListener l, 
            final long timeout, final TimeUnit unit) {
        
        synchronized (listeners) {
            
            if (!open || eof) {
                throw new IllegalStateException();                
            }
            
            if (timeout != -1L) {
                Runnable task = new Runnable() {
                    
                    private final long timeoutInMillis = unit.toMillis(timeout);
                    
                    @Override
                    public void run() {
                        long time = System.currentTimeMillis() - timeStamp;
                        if (time >= timeoutInMillis) {
                            synchronized (listeners) {
                                ScheduledFuture<?> future = listeners.get(l);
                                if (future != null) {
                                    future.cancel(true);
                                }
                            }
                            handleIdle(time, TimeUnit.MILLISECONDS);
                        }
                    }
                };
                
                ScheduledFuture<?> future = EXECUTOR.scheduleWithFixedDelay(
                        task, timeout, timeout, unit);
                
                ScheduledFuture<?> existing = listeners.put(l, future);
                if (existing != null) {
                    existing.cancel(true);
                }
                
            } else {
                listeners.putIfAbsent(l, null);
            }
        }
    }
    
    public void removeIdleListener(IdleListener l) {
        synchronized(listeners) {
            ScheduledFuture<?> future = listeners.remove(l);
            if (future != null) {
                future.cancel(true);
            }
        }
    }
    
    private void handleIdle(long time, TimeUnit unit) {
        fireIdle(time, unit);
    }
    
    private void handleEOF() {
        fireEOF();
    }
    
    private void handleClosed() {
        fireClosed();
    }
    
    protected void fireIdle(final long time, final TimeUnit unit) {
        if (!listeners.isEmpty()) {
            Runnable event = new Runnable() {
                @Override
                public void run() {
                    for (IdleListener l : listeners.keySet()) {
                        l.handleIdle(IdleInputStream.this, time, unit);
                    }
                }
            };
            EventUtils.fireEvent(event);
        }
    }
    
    protected void fireEOF() {
        if (!listeners.isEmpty()) {
            Runnable event = new Runnable() {
                @Override
                public void run() {
                    for (IdleListener l : listeners.keySet()) {
                        l.handleEOF(IdleInputStream.this);
                    }
                }
            };
            EventUtils.fireEvent(event);
        }
    }
    
    protected void fireClosed() {
        if (!listeners.isEmpty()) {
            Runnable event = new Runnable() {
                @Override
                public void run() {
                    for (IdleListener l : listeners.keySet()) {
                        l.handleClosed(IdleInputStream.this);
                    }
                }
            };
            EventUtils.fireEvent(event);
        }
    }
    
    public static interface IdleListener {
        
        public void handleIdle(IdleInputStream in, long time, TimeUnit unit);
        
        public void handleEOF(IdleInputStream in);
        
        public void handleClosed(IdleInputStream in);
    }
    
    public static abstract class IdleAdapter implements IdleListener {
        @Override
        public void handleIdle(IdleInputStream in, long time, TimeUnit unit) {
            IoUtils.close(in);
        }

        @Override
        public void handleEOF(IdleInputStream in) {
            IoUtils.close(in);
        }
    }
}
