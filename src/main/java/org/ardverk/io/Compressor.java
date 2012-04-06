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

import java.io.IOException;

/**
 * A {@link Compressor} is a simple byte-array based compression facility.
 */
public interface Compressor {
  
  /**
   * Returns the {@link Compressor}'s algorithm.
   */
  public String getAlgorithm();
  
  /**
   * Compresses the given byte-array.
   */
  public byte[] compress(byte[] value) throws IOException;
  
  /**
   * Compresses the given byte-array.
   */
  public byte[] compress(byte[] value, 
      int offset, int length) throws IOException;
  
  /**
   * Decompresses the given byte-array.
   */
  public byte[] decompress(byte[] value) throws IOException;
  
  /**
   * Decompresses the given byte-array.
   */
  public byte[] decompress(byte[] value, 
      int offset, int length) throws IOException;
}