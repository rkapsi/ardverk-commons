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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

  public static final int BUFFER_SIZE = 8 * 1024;
  
  private StreamUtils() {}
  
  public static long copy(InputStream in, 
      OutputStream out) throws IOException {
    return copy(in, out, BUFFER_SIZE);
  }
  
  public static long copy(InputStream in, OutputStream out, 
      int bufferSize) throws IOException {
    return copy(in, out, new byte[bufferSize]);
  }
  
  public static long copy(InputStream in, OutputStream out, 
      byte[] buffer) throws IOException {
    return copy(in, out, buffer, -1L);
  }
  
  public static long copy(InputStream in, OutputStream out, 
      long length) throws IOException {
    return copy(in, out, BUFFER_SIZE, length);
  }
  
  public static long copy(InputStream in, OutputStream out, 
      int bufferSize, long length) throws IOException {
    return copy(in, out, new byte[bufferSize], length);
  }
  
  public static long copy(InputStream in, OutputStream out, 
      byte[] buffer, long length) throws IOException {
    
    long total = 0L;
    
    int remaining = -1;
    int len = -1;
    
    while (total < length || length == -1L) {
      
      remaining = buffer.length;
      if (length != -1L) {
        remaining = (int)Math.min(length-total, buffer.length);
      }
      
      len = in.read(buffer, 0, remaining);
      if (len == -1) {
        if (length != -1) {
          throw new EOFException();
        }
        break;
      }
      
      out.write(buffer, 0, len);
      total += len;
    }
    
    return total;
  }
  
  public static byte[] readFully(InputStream in, byte[] dst) throws IOException {
    return readFully(in, dst, 0, dst.length);
  }
  
  public static byte[] readFully(InputStream in, 
      byte[] dst, int offset, int length) throws IOException {
    
    int total = 0;
    while (total < length) {
      int r = in.read(dst, offset + total, length - total);
      if (r == -1) {
        throw new EOFException();
      }
      total += r;
    }
    
    return dst;
  }
}
