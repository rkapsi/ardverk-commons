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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.ardverk.concurrent.ExecutorUtils;
import org.ardverk.concurrent.ManagedRunnable;

public abstract class OutputInputStream extends FilterOutputStream {

    private static final Executor EXECUTOR 
        = ExecutorUtils.newCachedThreadPool("OutputInputStreamThread");
    
    private static final int INIT = 0;
    
    private static final int READY = 1;
    
    private static final int CLOSED = 2;
    
    private final AtomicInteger state = new AtomicInteger(INIT);
    
    private final Object lock = new Object();
    
    private final PipedInputStream in;
    
    private final Runnable task = new ManagedRunnable() {
        
        @Override
        protected void doRun() throws IOException {
            synchronized (lock) {
                try {
                    try {
                        consume(in);
                    } finally {
                        IoUtils.close(in);
                    }
                } finally {
                    consumed = true;
                    lock.notifyAll();
                }
            }
        }

        @Override
        protected void exceptionCaught(Throwable t) {
            uncaughtException(t);
        }
    };
    
    private boolean consumed = false;
    
    public OutputInputStream() {
        this(-1);
    }
    
    public OutputInputStream(int bufferSize) {
        super(new PipedOutputStream());
        
        if (bufferSize == -1) {
            in = new PipedInputStream();
        } else {
            in = new PipedInputStream(bufferSize);
        }
        
        try {
            ((PipedOutputStream)out).connect(in);
        } catch (IOException err) {
            throw new IllegalStateException(err);
        }
    }
    
    private void execute() {
        if (state.compareAndSet(INIT, READY)) {
            boolean success = false;
            try {
                EXECUTOR.execute(task);
                success = true;
            } finally {
                if (!success) {
                    synchronized (lock) {
                        consumed = true;
                        lock.notifyAll();
                    }
                }
            }
        }
    }
    
    @Override
    public void write(int b) throws IOException {
        execute();
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        execute();
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        execute();
        super.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        execute();
        super.flush();
    }
    
    @Override
    public void close() throws IOException {
        try {
            close(true);
        } catch (InterruptedException err) {
            throw new IOException("InterruptedException", err);
        }
    }
    
    public void close(boolean await) throws IOException, InterruptedException {
        int wasState = state.getAndSet(CLOSED);
        if (wasState != CLOSED) {
            try {
                super.close();
                
                if (await && wasState == READY) {
                    synchronized (lock) {
                        if (!consumed) {
                            lock.wait();
                        }
                    }
                }
                
            } finally {
                IoUtils.close(in);
            }
        }
    }

    protected abstract void consume(InputStream in) throws IOException;
    
    protected void uncaughtException(Throwable t) {
    }
}
