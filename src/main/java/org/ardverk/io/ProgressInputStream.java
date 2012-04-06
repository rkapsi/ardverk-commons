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

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends FilterInputStream {

  private static final ProgressCallback DEFAULT = new ProgressAdapter();
  
  protected final ProgressCallback callback;
  
  private volatile boolean open = true;
  
  private volatile boolean eof = false;
  
  public ProgressInputStream(InputStream in) {
    this(in, DEFAULT);
  }
  
  public ProgressInputStream(InputStream in, ProgressCallback callback) {
    super(in);
    this.callback = callback;
  }
  
  public boolean isOpen() {
    return open;
  }
  
  public boolean isEof() {
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
      process(value, 1);
    }
    return value;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int r = init();
    try {
      r = super.read(b, off, len);
    } finally {
      process(r, r);
    }
    return r;
  }

  private void process(int r, int count) {
    if (r == -1) {
      if (!eof) {
        eof = true;
        eof();
      }
    } else {
      in(count);
    }
  }
  
  @Override
  public void close() throws IOException {   
    if (open) {
      open = false;
      try {
        super.close();
      } finally {
        closed();
      }
    }
  }
  
  /**
   * Called every time some data has been read from 
   * the {@link ProgressInputStream}.
   * 
   * @see #read()
   * @see #read(byte[])
   * @see #read(byte[], int, int)
   */
  protected void in(int count) {
    callback.in(this, count);
  }

  /**
   * Called if the {@link ProgressInputStream}'s EOF has been reached.
   */
  protected void eof() {
    callback.eof(this);
  }
  
  /**
   * Called if the {@link ProgressInputStream} has been closed.
   * 
   * @see #close()
   */
  protected void closed() {
    callback.closed(this);
  }
  
  /**
   * 
   */
  public static interface ProgressCallback {
    /**
     * Called every time some data has been read from 
     * the {@link ProgressInputStream}.
     */
    public void in(InputStream in, int count);
    
    /**
     * Called if the {@link ProgressInputStream}'s EOF has been reached.
     */
    public void eof(InputStream in);
    
    /**
     * Called if the {@link ProgressInputStream} has been closed.
     * 
     * @see ProgressInputStream#close()
     */
    public void closed(InputStream in);
  }
  
  /**
   * 
   */
  public static class ProgressAdapter implements ProgressCallback {
    @Override
    public void in(InputStream in, int count) {
    }

    @Override
    public void eof(InputStream in) {
    }

    @Override
    public void closed(InputStream in) {
    }
  }
}
