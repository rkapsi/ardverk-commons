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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.ardverk.concurrent.ExecutorUtils;

public class InputOutputStream extends FilterInputStream {
    
    private static final Executor EXECUTOR 
        = ExecutorUtils.newCachedThreadPool("InputOutputStreamThread");
    
    private static final int INIT = 0;
    
    private static final int READY = 1;
    
    private static final int CLOSED = 2;
    
    private final PipedOutputStream out = new PipedOutputStream();
    
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            try {
                writeTo(out);
            } catch (IOException err) {
                uncaughtException(err);
            } finally {
                IoUtils.close(out);
            }
        }
    };
    
    private final AtomicInteger state = new AtomicInteger(INIT);
    
    public InputOutputStream() {
        super(new PipedInputStream());
        connect();
    }
    
    public InputOutputStream(int bufferSize) {
        super(new PipedInputStream(bufferSize));
        connect();
    }
    
    private void connect() {
        try {
            ((PipedInputStream)in).connect(out);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void execute() {
        if (state.compareAndSet(INIT, READY)) {
            EXECUTOR.execute(task);
        }
    }
    
    @Override
    public int read() throws IOException {
        execute();
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        execute();
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        execute();
        return super.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        if (state.getAndSet(CLOSED) != CLOSED) {
            IoUtils.close(out);
            super.close();
        }
    }
    
    public void writeTo(OutputStream out) throws IOException {
        throw new IOException();
    }
    
    public void uncaughtException(Throwable t) {
    }
}
