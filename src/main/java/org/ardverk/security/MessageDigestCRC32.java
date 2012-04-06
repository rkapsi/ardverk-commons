/*
 * Copyright 2010-2012 Roger Kapsi
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

package org.ardverk.security;

import java.security.MessageDigest;
import java.util.zip.CRC32;

/**
 * An implementation of {@link MessageDigest} that is computing 
 * a {@link CRC32} value.
 * 
 * <p>NOTE: A {@link CRC32} is not a cryptographic hash!
 */
public class MessageDigestCRC32 extends MessageDigest {

  /**
   * The name of the algorithm.
   */
  public static final String NAME = "CRC32";
  
  /**
   * The length of the hash in bytes.
   */
  public static final int LENGTH = 4;
  
  private final CRC32 crc = new CRC32();
  
  public MessageDigestCRC32() {
    super(NAME);
  }
  
  @Override
  protected int engineGetDigestLength() {
    return LENGTH;
  }
  
  @Override
  protected byte[] engineDigest() {
    long value = crc.getValue();
    return new byte[] {
      (byte)((value >> 24L) & 0xFF),
      (byte)((value >> 16L) & 0xFF),
      (byte)((value >>  8L) & 0xFF),
      (byte)((value     ) & 0xFF),
    };
  }

  @Override
  protected void engineReset() {
    crc.reset();
  }

  @Override
  protected void engineUpdate(byte input) {
    crc.update(input);
  }

  @Override
  protected void engineUpdate(byte[] input, int offset, int len) {
    crc.update(input, offset, len);
  }
}