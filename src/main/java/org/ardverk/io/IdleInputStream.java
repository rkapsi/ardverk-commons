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
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.EventUtils;
import org.ardverk.utils.Idle;
import org.ardverk.utils.Idle.IdleListener;

/**
 * The {@link IdleInputStream} fires events if nothing have been read
 * for this {@link InputStream} for a given period of time. It's also
 * firing EOF and CLOSE events.
 */
public class IdleInputStream extends FilterInputStream {
    
    private static final IdleInputStreamListener DEFAULT = new IdleInputStreamAdapter() {
        @Override
        public void handleClosed(IdleInputStream in) {
        }
    };
    
    private final ConcurrentHashMap<IdleInputStreamListener, IdleListener> listeners 
        = new ConcurrentHashMap<IdleInputStreamListener, IdleListener>();
    
    private final Idle idle;
    
    private boolean open = true;
    
    private boolean eof = false;
    
    public IdleInputStream(InputStream in) {
        this(in, 5L, TimeUnit.SECONDS);
    }
    
    public IdleInputStream(InputStream in, long frequency, TimeUnit unit) {
        super(in);
        
        this.idle = new Idle(frequency, unit);
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
            idle.touch();
        }
    }
    
    @Override
    public void close() throws IOException {   
        if (open) {
            
            synchronized (listeners) {
                open = false;
                idle.close();
            }
            
            try {
                super.close();
            } finally {
                handleClosed();
            }
        }
    }
    
    public void addIdleInputStreamListener(IdleInputStreamListener l) {
        addIdleInputStreamListener(l, -1L, TimeUnit.MILLISECONDS);
    }
    
    public void addIdleInputStreamListener(long timeout, TimeUnit unit) {
        addIdleInputStreamListener(DEFAULT, timeout, unit);
    }
    
    public void addIdleInputStreamListener(final IdleInputStreamListener l, 
            final long timeout, final TimeUnit unit) {
        
        synchronized (listeners) {
            
            if (!open || eof) {
                throw new IllegalStateException();                
            }
            
            if (timeout != -1L) {
                IdleListener delegate = new IdleListener() {
                    @Override
                    public void handleIdle(IdleListener l, long timeout, TimeUnit unit) {
                        IdleInputStream.this.handleIdle(timeout, unit);
                    }
                };
                
                idle.addIdleListener(delegate, timeout, unit);
                IdleListener existing = listeners.put(l, delegate);
                
                if (existing != null) {
                    idle.removeIdleListener(existing);
                }
                
            } else {
                listeners.putIfAbsent(l, null);
            }
        }
    }
    
    public void removeIdleInputStreamListener(IdleInputStreamListener l) {
        synchronized(listeners) {
            IdleListener delegate = listeners.remove(l);
            if (delegate != null) {
                idle.removeIdleListener(delegate);
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
                    for (IdleInputStreamListener l : listeners.keySet()) {
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
                    for (IdleInputStreamListener l : listeners.keySet()) {
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
                    for (IdleInputStreamListener l : listeners.keySet()) {
                        l.handleClosed(IdleInputStream.this);
                    }
                }
            };
            EventUtils.fireEvent(event);
        }
    }
    
    public static interface IdleInputStreamListener {
        
        public void handleIdle(IdleInputStream in, long time, TimeUnit unit);
        
        public void handleEOF(IdleInputStream in);
        
        public void handleClosed(IdleInputStream in);
    }
    
    public static abstract class IdleInputStreamAdapter 
            implements IdleInputStreamListener {
        
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
