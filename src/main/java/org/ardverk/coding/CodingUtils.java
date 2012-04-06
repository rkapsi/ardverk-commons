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

package org.ardverk.coding;

import org.ardverk.utils.StringUtils;

/**
 * An utility class for different Base codings.
 */
public class CodingUtils {

  private CodingUtils() {}
  
  /**
   * Encodes the given {@link String} in Base-2 (binary) and returns it 
   * as a {@link String}.
   */
  public static String encodeBase2(String data) {
    return encodeBase2(StringUtils.getBytes(data));
  }
  
  /**
   * Encodes the given byte-array in Base-2 (binary) and returns it 
   * as a {@link String}.
   */
  public static String encodeBase2(byte[] data) {
    return encodeBase2(data, 0, data.length);
  }
  
  /**
   * Encodes the given byte-array in Base-2 (binary) and returns it 
   * as a {@link String}.
   */
  public static String encodeBase2(byte[] data, int offset, int length) {
    return StringUtils.toString(Base2.encodeBase2(data, offset, length));
  }

  /**
   * Encodes the given {@link String} in Base-16 (hex) and returns it 
   * as a {@link String}.
   */
  public static String encodeBase16(String data) {
    return encodeBase16(StringUtils.getBytes(data));
  }
  
  /**
   * Encodes the given byte-array in Base-16 (hex) and returns it 
   * as a {@link String}.
   */
  public static String encodeBase16(byte[] data) {
    return encodeBase16(data, 0, data.length);
  }
  
  /**
   * Encodes the given byte-array in Base-16 (hex) and returns it 
   * as a {@link String}.
   */
  public static String encodeBase16(byte[] data, int offset, int length) {
    return StringUtils.toString(Base16.encodeBase16(data, offset, length));
  }
  
  /**
   * Decodes a Base-16 (hex) encoded {@link String}.
   */
  public static byte[] decodeBase16(String data) {
    return decodeBase16(StringUtils.getBytes(data));
  }
  
  /**
   * Decodes a Base-16 (hex) encoded value.
   */
  public static byte[] decodeBase16(byte[] data) {
    return decodeBase16(data, 0, data.length);
  }
  
  /**
   * Decodes a Base-16 (hex) encoded value.
   */
  public static byte[] decodeBase16(byte[] data, int offset, int length) {
    return Base16.decodeBase16(data, offset, length);
  }
}