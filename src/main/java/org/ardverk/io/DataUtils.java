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

package org.ardverk.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A collection of binary operations.
 * 
 * Conventions:
 * 
 * beb = Big Endian Bytes
 * leb = Little Endian Bytes
 */
public class DataUtils {

  /**
   * The size of a {@code byte} in bytes.
   */
  public static final int BYTE = Byte.SIZE / Byte.SIZE;
  
  /**
   * The size of a {@code boolean} in bytes.
   */
  public static final int BOOLEAN = BYTE;
  
  /**
   * The size of a {@code short} in bytes.
   */
  public static final int SHORT = Short.SIZE / Byte.SIZE;
  
  /**
   * The size of a {@code int} in bytes.
   */
  public static final int INT = Integer.SIZE / Byte.SIZE;
  
  /**
   * The size of a {@code float} in bytes.
   */
  public static final int FLOAT = Float.SIZE / Byte.SIZE;
  
  /**
   * The size of a {@code long} in bytes.
   */
  public static final int LONG = Long.SIZE / Byte.SIZE;
  
  /**
   * The size of a {@code double} in bytes.
   */
  public static final int DOUBLE = Double.SIZE / Byte.SIZE;
  
  /**
   * Constant for {@code true}.
   */
  private static final byte TRUE = 1;
  
  /**
   * Constant for {@code false}.
   */
  private static final byte FALSE = 0;
  
  private DataUtils() {}
  
  /**
   * Reads a single {@code byte} from the given {@link InputStream}
   * and returns it or throws an {@link EOFException} if the EOF has
   * been reached.
   */
  public static int read(InputStream in) throws IOException {
    int value = in.read();
    if (value == -1) {
      throw new EOFException();
    }
    return value;
  }
  
  public static boolean bool(InputStream in) throws IOException {
    return read(in) != FALSE;
  }
  
  public static boolean bool(byte[] value) throws IOException {
    return bool(value, 0);
  }
  
  public static boolean bool(byte[] value, int offset) throws IOException {
    return value[offset] != FALSE;
  }
  
  public static void bool(boolean value, OutputStream out) throws IOException {
    out.write(value ? TRUE : FALSE);
  }
  
  public static byte[] bool(boolean value) throws IOException {
    return bool(value, new byte[BOOLEAN]);
  }
  
  public static byte[] bool(boolean value, byte[] dst) throws IOException {
    return bool(value, dst, 0);
  }
  
  public static byte[] bool(boolean value, byte[] dst, int offset) throws IOException {
    dst[offset] = value ? TRUE : FALSE;
    return dst;
  }
  
  public static int beb2ushort(InputStream in) throws IOException {
    return (read(in) << 8) | read(in);
  }
  
  public static int beb2ushort(byte[] value) {
    return beb2ushort(value, 0);
  }
  
  public static int beb2ushort(byte[] value, int offset) {
    return ((value[offset  ] & 0xFF) << 8) 
      |  ((value[offset + 1] & 0xFF)   );
  }
  
  public static int leb2ushort(InputStream in) throws IOException {
    return read(in) | (read(in) << 8);
  }
  
  public static int leb2ushort(byte[] value) {
    return leb2ushort(value, 0);
  }
  
  public static int leb2ushort(byte[] value, int offset) {
    return ((value[offset + 1] & 0xFF) << 8) 
      |  ((value[offset  ] & 0xFF)   );
  }
  
  public static short beb2short(InputStream in) throws IOException {
    return (short)beb2ushort(in);
  }
  
  public static short beb2short(byte[] value) {
    return (short)beb2ushort(value);
  }
  
  public static short beb2short(byte[] value, int offset) {
    return (short)beb2ushort(value, offset);
  }
  
  public static short leb2short(InputStream in) throws IOException {
    return (short)leb2ushort(in);
  }
  
  public static short leb2short(byte[] value) {
    return (short)leb2ushort(value);
  }
  
  public static short leb2short(byte[] value, int offset) {
    return (short)leb2ushort(value, offset);
  }
  
  public static int beb2int(InputStream in) throws IOException {
    return (read(in) << 24) | (read(in) << 16) 
      |  (read(in) <<  8) | (read(in)    );
  }
  
  public static int beb2int(byte[] value) {
    return beb2int(value, 0);
  }
  
  public static int beb2int(byte[] value, int offset) {
    return ((value[offset  ] & 0xFF) << 24) 
      |  ((value[offset + 1] & 0xFF) << 16) 
      |  ((value[offset + 2] & 0xFF) <<  8) 
      |  ((value[offset + 3] & 0xFF)    );
  }
  
  public static int leb2int(InputStream in) throws IOException {
    return (read(in)    ) | (read(in) <<  8) 
      |  (read(in) << 16) | (read(in) << 24);
  }
  
  public static int leb2int(byte[] value) {
    return leb2int(value, 0);
  }
  
  public static int leb2int(byte[] value, int offset) {
    return ((value[offset + 3] & 0xFF) << 24) 
      |  ((value[offset + 2] & 0xFF) << 16) 
      |  ((value[offset + 1] & 0xFF) <<  8) 
      |  ((value[offset  ] & 0xFF)    );
  }
  
  public static long beb2long(InputStream in) throws IOException {
    return (read(in) << 56L) | (read(in) << 48L) 
      |  (read(in) << 40L) | (read(in) << 32L) 
      |  (read(in) << 24L) | (read(in) << 16L) 
      |  (read(in) <<  8L) | (read(in)     );
  }
  
  public static long beb2long(byte[] value) {
    return beb2long(value, 0);
  }
  
  public static long beb2long(byte[] value, int offset) {
    return ((value[offset  ] & 0xFFL) << 56L) 
      |  ((value[offset + 1] & 0xFFL) << 48L) 
      |  ((value[offset + 2] & 0xFFL) << 40L) 
      |  ((value[offset + 3] & 0xFFL) << 32L) 
      |  ((value[offset + 4] & 0xFFL) << 24L) 
      |  ((value[offset + 5] & 0xFFL) << 16L) 
      |  ((value[offset + 6] & 0xFFL) <<  8L) 
      |  ((value[offset + 7] & 0xFFL)     );
  }
  
  public static long leb2long(InputStream in) throws IOException {
    return (read(in)     ) | (read(in) <<  8L) 
      |  (read(in) << 16L) | (read(in) << 24L) 
      |  (read(in) << 32L) | (read(in) << 40L) 
      |  (read(in) << 48L) | (read(in) << 56L);
  }
  
  public static long leb2long(byte[] value) {
    return leb2long(value, 0);
  }
  
  public static long leb2long(byte[] value, int offset) {
    return ((value[offset + 7] & 0xFFL) << 56L) 
      |  ((value[offset + 6] & 0xFFL) << 48L) 
      |  ((value[offset + 5] & 0xFFL) << 40L) 
      |  ((value[offset + 4] & 0xFFL) << 32L) 
      |  ((value[offset + 3] & 0xFFL) << 24L) 
      |  ((value[offset + 2] & 0xFFL) << 16L) 
      |  ((value[offset + 1] & 0xFFL) <<  8L) 
      |  ((value[offset  ] & 0xFFL)     );
  }
  
  public static float beb2float(InputStream in) throws IOException {
    return Float.intBitsToFloat(beb2int(in));
  }
  
  public static float beb2float(byte[] value) {
    return beb2float(value, 0);
  }
  
  public static float beb2float(byte[] value, int offset) {
    return Float.intBitsToFloat(beb2int(value, offset));
  }
  
  public static float leb2float(InputStream in) throws IOException {
    return Float.intBitsToFloat(leb2int(in));
  }
  
  public static float leb2float(byte[] value) {
    return leb2float(value, 0);
  }
  
  public static float leb2float(byte[] value, int offset) {
    return Float.intBitsToFloat(leb2int(value, offset));
  }
  
  public static double beb2double(InputStream in) throws IOException {
    return Double.longBitsToDouble(beb2long(in));
  }
  
  public static double beb2double(byte[] value) {
    return beb2double(value, 0);
  }
  
  public static double beb2double(byte[] value, int offset) {
    return Double.longBitsToDouble(beb2long(value, offset));
  }
  
  public static double leb2double(InputStream in) throws IOException {
    return Double.longBitsToDouble(leb2long(in));
  }
  
  public static double leb2double(byte[] value) {
    return leb2double(value, 0);
  }
  
  public static double leb2double(byte[] value, int offset) {
    return Double.longBitsToDouble(leb2long(value, offset));
  }
  
  public static OutputStream short2beb(int value, OutputStream out) throws IOException {
    out.write(value >>>  8);
    out.write(value     );
    return out;
  }
  
  public static byte[] short2beb(int value) {
    return short2beb(value, new byte[SHORT]);
  }
  
  public static byte[] short2beb(int value, byte[] dst) {
    return short2beb(value, dst, 0);
  }
  
  public static byte[] short2beb(int value, byte[] dst, int offset) {
    dst[offset  ] = (byte)(value >>> 8);
    dst[offset + 1] = (byte)(value    );
    return dst;
  }
  
  public static OutputStream short2leb(int value, OutputStream out) throws IOException {
    out.write(value     );
    out.write(value >>>  8);
    return out;
  }
  
  public static byte[] short2leb(int value) {
    return short2leb(value, new byte[SHORT]);
  }
  
  public static byte[] short2leb(int value, byte[] dst) {
    return short2leb(value, dst, 0);
  }
  
  public static byte[] short2leb(int value, byte[] dst, int offset) {
    dst[offset + 1] = (byte)(value >>> 8);
    dst[offset  ] = (byte)(value    );
    return dst;
  }
  
  public static OutputStream int2beb(int value, OutputStream out) throws IOException {
    out.write(value >>> 24);
    out.write(value >>> 16);
    out.write(value >>>  8);
    out.write(value     );
    return out;
  }
  
  public static byte[] int2beb(int value) {
    return int2beb(value, new byte[INT]);
  }
  
  public static byte[] int2beb(int value, byte[] dst) {
    return int2beb(value, dst, 0);
  }
  
  public static byte[] int2beb(int value, byte[] dst, int offset) {
    dst[offset  ] = (byte)(value >>> 24);
    dst[offset + 1] = (byte)(value >>> 16);
    dst[offset + 2] = (byte)(value >>>  8);
    dst[offset + 3] = (byte)(value     );
    return dst;
  }
  
  public static OutputStream int2leb(int value, OutputStream out) throws IOException {
    out.write(value     );
    out.write(value >>>  8);
    out.write(value >>> 16);
    out.write(value >>> 24);
    return out;
  }
  
  public static byte[] int2leb(int value) {
    return int2leb(value, new byte[INT]);
  }
  
  public static byte[] int2leb(int value, byte[] dst) {
    return int2leb(value, dst, 0);
  }
  
  public static byte[] int2leb(int value, byte[] dst, int offset) {
    dst[offset + 3] = (byte)(value >>> 24);
    dst[offset + 2] = (byte)(value >>> 16);
    dst[offset + 1] = (byte)(value >>>  8);
    dst[offset  ] = (byte)(value     );
    return dst;
  }
  
  public static OutputStream long2beb(long value, OutputStream out) throws IOException {
    out.write((int)(value >>> 56L));
    out.write((int)(value >>> 48L));
    out.write((int)(value >>> 40L));
    out.write((int)(value >>> 32L));
    out.write((int)(value >>> 24L));
    out.write((int)(value >>> 16L));
    out.write((int)(value >>>  8L));
    out.write((int)(value    ));
    return out;
  }
  
  public static byte[] long2beb(long value) {
    return long2beb(value, new byte[LONG]);
  }
  
  public static byte[] long2beb(long value, byte[] dst) {
    return long2beb(value, dst, 0);
  }
  
  public static byte[] long2beb(long value, byte[] dst, int offset) {
    dst[offset  ] = (byte)(value >>> 56L);
    dst[offset + 1] = (byte)(value >>> 48L);
    dst[offset + 2] = (byte)(value >>> 40L);
    dst[offset + 3] = (byte)(value >>> 32L);
    dst[offset + 4] = (byte)(value >>> 24L);
    dst[offset + 5] = (byte)(value >>> 16L);
    dst[offset + 6] = (byte)(value >>>  8L);
    dst[offset + 7] = (byte)(value    );
    return dst;
  }
  
  public static OutputStream long2leb(long value, OutputStream out) throws IOException {
    out.write((int)(value    ));
    out.write((int)(value >>>  8L));
    out.write((int)(value >>> 16L));
    out.write((int)(value >>> 24L));
    out.write((int)(value >>> 32L));
    out.write((int)(value >>> 40L));
    out.write((int)(value >>> 48L));
    out.write((int)(value >>> 56L));
    return out;
  }
  
  public static byte[] long2leb(long value) {
    return long2leb(value, new byte[LONG]);
  }
  
  public static byte[] long2leb(long value, byte[] dst) {
    return long2leb(value, dst, 0);
  }
  
  public static byte[] long2leb(long value, byte[] dst, int offset) {
    dst[offset + 7] = (byte)(value >>> 56L);
    dst[offset + 6] = (byte)(value >>> 48L);
    dst[offset + 5] = (byte)(value >>> 40L);
    dst[offset + 4] = (byte)(value >>> 32L);
    dst[offset + 3] = (byte)(value >>> 24L);
    dst[offset + 2] = (byte)(value >>> 16L);
    dst[offset + 1] = (byte)(value >>>  8L);
    dst[offset  ] = (byte)(value    );
    return dst;
  }
  
  public static OutputStream float2beb(float value, OutputStream out) throws IOException {
    return int2beb(Float.floatToIntBits(value), out);
  }
  
  public static byte[] float2beb(float value) {
    return float2beb(value, new byte[FLOAT]);
  }
  
  public static byte[] float2beb(float value, byte[] dst) {
    return float2beb(value, dst, 0);
  }
  
  public static byte[] float2beb(float value, byte[] dst, int offset) {
    return int2beb(Float.floatToIntBits(value), dst, offset);
  }
  
  public static OutputStream float2leb(float value, OutputStream out) throws IOException {
    return int2leb(Float.floatToIntBits(value), out);
  }
  
  public static byte[] float2leb(float value) {
    return float2leb(value, new byte[FLOAT]);
  }
  
  public static byte[] float2leb(float value, byte[] dst) {
    return float2leb(value, dst, 0);
  }
  
  public static byte[] float2leb(float value, byte[] dst, int offset) {
    return int2leb(Float.floatToIntBits(value), dst, offset);
  }
  
  public static OutputStream double2beb(double value, OutputStream out) throws IOException {
    return long2beb(Double.doubleToLongBits(value), out);
  }
  
  public static byte[] double2beb(double value) {
    return double2beb(value, new byte[DOUBLE]);
  }
  
  public static byte[] double2beb(double value, byte[] dst) {
    return double2beb(value, dst, 0);
  }
  
  public static byte[] double2beb(double value, byte[] dst, int offset) {
    return long2beb(Double.doubleToLongBits(value), dst, offset);
  }
  
  public static OutputStream double2leb(double value, OutputStream out) throws IOException {
    return long2leb(Double.doubleToLongBits(value), out);
  }
  
  public static byte[] double2leb(double value) {
    return double2leb(value, new byte[DOUBLE]);
  }
  
  public static byte[] double2leb(double value, byte[] dst) {
    return double2leb(value, dst, 0);
  }
  
  public static byte[] double2leb(double value, byte[] dst, int offset) {
    return long2leb(Double.doubleToLongBits(value), dst, offset);
  }
  
  public static int ubyte2int(byte value) {
    return value & 0xFF;
  }
  
  public static int ushort2int(short value) {
    return value & 0xFFFF;
  }
  
  public static long uint2long(int value) {
    return value & 0xFFFFFFFFL;
  }
  
  public static void int2vbeb(int value, OutputStream out) throws IOException {
    while ((value & ~0x7F) != 0) {
      out.write((value & 0x7f) | 0x80);
      value >>>= 7;
      }
      out.write(value);
  }
  
  public static int vbeb2int(InputStream in) throws IOException {
    int b = read(in);
    int i = b & 0x7F;
    if ((b & 0x80) == 0) return i;
    b = read(in);
    i |= (b & 0x7F) << 7;
    if ((b & 0x80) == 0) return i;
    b = read(in);
    i |= (b & 0x7F) << 14;
    if ((b & 0x80) == 0) return i;
    b = read(in);
    i |= (b & 0x7F) << 21;
    if ((b & 0x80) == 0) return i;
    b = read(in);
    assert (b & 0x80) == 0;
    return i | ((b & 0x7F) << 28);
  }
}
