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

package org.ardverk.lang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.ardverk.coding.CodingUtils;
import org.ardverk.io.Streamable;
import org.ardverk.utils.ByteArrayComparator;

public class ByteArray<T extends ByteArray<T>> 
    implements Comparable<T>, Streamable, Serializable {
  
  private static final long serialVersionUID = -3599578418695385540L;
  
  protected final byte[] value;
  
  private int hashCode = 0;

  public ByteArray(byte[] value) {
    this.value = value;
  }
  
  /**
   * Returns the length of the {@link ByteArray} in {@code byte}s.
   */
  public int length() {
    return value.length;
  }
  
  /**
   * Returns a copy of the underlying {@code byte[]}.
   */
  public byte[] getBytes() {
    return getBytes(true);
  }
  
  /**
   * Returns the underlying {@code byte[]}.
   */
  public byte[] getBytes(boolean copy) {
    return copy ? value.clone() : value;
  }
  
  /**
   * Copies and returns the underlying {@code byte[]}.
   */
  public byte[] getBytes(byte[] dst, int destPos) {
    assert (dst != value); // Who would be so stupid?
    System.arraycopy(value, 0, dst, destPos, value.length);
    return dst;
  }
  
  @Override
  public void writeTo(OutputStream out) throws IOException {
    out.write(value);
  }
  
  @Override
  public int compareTo(T o) {
    return ByteArrayComparator.COMPARATOR.compare(value, o.value);
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      hashCode = Arrays.hashCode(value);
    }
    return hashCode;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ByteArray<?>)) {
      return false;
    }
    
    ByteArray<?> other = (ByteArray<?>)o;
    return Arrays.equals(value, other.value);
  }
  
  public String toBinString() {
    return CodingUtils.encodeBase2(value);
  }
  
  public String toHexString() {
    return CodingUtils.encodeBase16(value);
  }
  
  @Override
  public String toString() {
    return toHexString();
  }
}
