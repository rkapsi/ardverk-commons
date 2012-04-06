/*
 * Copyright 2010-2011 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.concurrent.atomic.AtomicReference;

import org.ardverk.concurrent.ExecutorUtils;
import org.ardverk.concurrent.ManagedRunnable;

public abstract class InputOutputStream extends FilterInputStream {
  
  private static final Executor EXECUTOR 
    = ExecutorUtils.newCachedThreadPool("InputOutputStreamThread");
  
  private static final Producer SELF = new Producer() {
    @Override
    public void produce(OutputStream out) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public void uncaughtException(Throwable t) {
      throw new UnsupportedOperationException();
    }
  };
  
  private static enum State {
    INIT,
    READY,
    CLOSED;
  }
  
  private final PipedOutputStream out = new PipedOutputStream();
  
  private final Runnable task = new ManagedRunnable() {
    @Override
    protected void doRun() throws IOException {
      try {
        producer.produce(out);
      } finally {
        IoUtils.close(out);
      }
    }

    @Override
    protected void exceptionCaught(Throwable t) {
      producer.uncaughtException(t);
    }
  };
  
  private final AtomicReference<State> state 
    = new AtomicReference<State>(State.INIT);
  
  private final Producer producer;
  
  public InputOutputStream() {
    this(-1);
  }
  
  public InputOutputStream(Producer producer) {
    this(producer, -1);
  }
  
  public InputOutputStream(int bufferSize) {
    this(SELF, bufferSize);
  }
  
  public InputOutputStream(Producer producer, int bufferSize) {
    super(newPipedInputStream(bufferSize));
    
    if (producer == null) {
      throw new NullPointerException("producer");
    }
    
    if (producer == SELF) {
      producer = new Producer() {
        @Override
        public void produce(OutputStream out) throws IOException {
          InputOutputStream.this.produce(out);
        }

        @Override
        public void uncaughtException(Throwable t) {
          InputOutputStream.this.uncaughtException(t);
        }
      };
    }
    
    this.producer = producer;
    
    try {
      ((PipedInputStream)in).connect(out);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
  
  private static PipedInputStream newPipedInputStream(int bufferSize) {
    if (bufferSize == -1) {
      return new PipedInputStream();
    }
    return new PipedInputStream(bufferSize);
  }
  
  private void execute() {
    if (state.compareAndSet(State.INIT, State.READY)) {
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
    if (state.getAndSet(State.CLOSED) != State.CLOSED) {
      IoUtils.close(out);
      super.close();
    }
  }
  
  public static interface Producer {
    public void produce(OutputStream out) throws IOException;
    
    public void uncaughtException(Throwable t);
  }
  
  protected abstract void produce(OutputStream out) throws IOException;
  
  protected void uncaughtException(Throwable t) {
  }
}
