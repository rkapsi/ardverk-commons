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

package org.ardverk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.ardverk.io.ByteUtils;

public class StringUtils {

  public static final String UTF8 = "UTF-8";
  
  public static final Charset UTF8_CHARSET = Charset.forName(UTF8);
  
  public static final String ASCII = "ISO-8859-1";
  
  public static final Charset ASCII_CHARSET = Charset.forName(ASCII);
  
  private StringUtils() {}
  
  public static String toString(byte[] data) {
    return toString(data, 0, data.length);
  }
  
  public static String toString(byte[] data, int offset, int length) {
    return toString(data, offset, length, UTF8_CHARSET);
  }
  
  public static String toString(byte[] data, String encoding) {
    return toString(data, Charset.forName(encoding));
  }
  
  public static String toString(byte[] data, Charset encoding) {
    return toString(data, 0, data.length, encoding);
  }
  
  public static String toString(byte[] data, int offset, int length, Charset encoding) {
    return new String(data, offset, length, encoding);
  }
  
  public static byte[] getBytes(String data) {
    return getBytes(data, UTF8_CHARSET);
  }
  
  public static byte[] getBytes(String data, String encoding) {
    return getBytes(data, Charset.forName(encoding));
  }
  
  public static byte[] getBytes(String data, Charset encoding) {
    return data.getBytes(encoding);
  }
  
  /**
   * Writes the {@link String} to the given {@link OutputStream}.
   */
  public static void writeString(String value, OutputStream out) throws IOException {
    writeString(value, UTF8_CHARSET, out);
  }
  
  /**
   * Writes the {@link String} to the given {@link OutputStream}.
   */
  public static void writeString(String value, String encoding, OutputStream out) throws IOException {
    writeString(value, Charset.forName(encoding), out);
  }
  
  /**
   * Writes the {@link String} to the given {@link OutputStream}.
   */
  public static void writeString(String value, Charset encoding, OutputStream out) throws IOException {
    byte[] data = getBytes(value, encoding);
    ByteUtils.writeBytes(data, out);
  }
  
  /**
   * Reads and returns a {@link String} from the given {@link InputStream}.
   */
  public static String readString(InputStream in) throws IOException {
    return readString(in, UTF8_CHARSET);
  }
  
  /**
   * Reads and returns a {@link String} from the given {@link InputStream}.
   */
  public static String readString(InputStream in, String encoding) throws IOException {
    return readString(in, Charset.forName(encoding));
  }
  
  /**
   * Reads and returns a {@link String} from the given {@link InputStream}.
   */
  public static String readString(InputStream in, Charset encoding) throws IOException {
    byte[] data = ByteUtils.readBytes(in);
    return toString(data, encoding);
  }

  /**
   * Returns true if the given {@link String} is {@code null}, is an
   * empty string or is just a bunch of whitespace characters as defined
   * by {@link Character#isWhitespace(char)}.
   */
  public static boolean isEmpty(String value) {
    if (value != null && !value.isEmpty()) {
      for (int i = value.length()-1; i >= 0; --i) {
        if (!Character.isWhitespace(value.charAt(i))) {
          return false;
        }
      }
    }
    
    return true;
  }
  
  /**
   * @see String#trim()
   */
  public static String trim(String value) {
    return trim(value, ' ');
  }
  
  public static String trim(String value, char ch, char... others) {
    if (value != null) {
      int length = value.length();
      
      int p = 0;
      int q = length;
      
      // Left to Right...
      for (int i = 0; i < length; i++) {
        if (!isCharAt(value, i, ch, others)) {
          break;
        }
        
        ++p;
      }
      
      // Right to Left...
      for (int i = length-1; p < i; --i) {
        if (!isCharAt(value, i, ch, others)) {
          break;
        }
        
        --q;
      }
      
      if (p != 0 || q != length) {
        return value.substring(p, q);
      }
    }
    
    return value;
  }
  
  public static boolean isCharAt(String value, int index, 
      char ch, char... others) {
    char actual = value.charAt(index);
    
    if (ch == actual) {
      return true;
    }
    
    int length = (others != null) ? others.length : 0;
    if (0 < length) {
      for (int i = length-1; i >= 0; --i) {
        if (actual == others[i]) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  public static String decodeURL(String value) {
    return decodeURL(value, UTF8);
  }
  
  public static String decodeURL(String value, String encoding) {
    try {
      return URLDecoder.decode(value, encoding);
    } catch (UnsupportedEncodingException err) {
      throw new IllegalArgumentException(
          "UnsupportedEncodingException", err);
    }
  }
  
  public static String encodeURL(String value) {
    return encodeURL(value, UTF8);
  }
  
  public static String encodeURL(String value, String encoding) {
    try {
      return URLEncoder.encode(value, encoding);
    } catch (UnsupportedEncodingException err) {
      throw new IllegalArgumentException(
          "UnsupportedEncodingException", err);
    }
  }
}