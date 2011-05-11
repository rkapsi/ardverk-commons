package org.ardverk.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataUtils {

    /**
     * The size of a {@code byte} in bytes.
     */
    public static final int BYTE = Byte.SIZE / Byte.SIZE;
    
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
    
    private DataUtils() {}
    
    private static int r(InputStream in) throws IOException {
        int value = in.read();
        if (value == -1) {
            throw new EOFException();
        }
        return value;
    }
    
    public static void readFully(InputStream in, byte[] b) throws IOException {
        readFully(in, b, 0, b.length);
    }
    
    public static void readFully(InputStream in, byte[] b, 
            int offset, int length) throws IOException {
        
        int total = 0;
        while (total < length) {
            int r = in.read(b, offset + total, length - total);
            if (r == -1) {
                throw new EOFException();
            }
            
            total += r;
        }
    }
    
    public static int beb2ushort(InputStream in) throws IOException {
        return (r(in) << 8) | r(in);
    }
    
    public static int beb2ushort(byte[] value) {
        return beb2ushort(value, 0);
    }
    
    public static int beb2ushort(byte[] value, int offset) {
        return ((value[offset    ] & 0xFF) << 8) 
            |  ((value[offset + 1] & 0xFF)     );
    }
    
    public static int leb2ushort(InputStream in) throws IOException {
        return r(in) | (r(in) << 8);
    }
    
    public static int leb2ushort(byte[] value) {
        return leb2ushort(value, 0);
    }
    
    public static int leb2ushort(byte[] value, int offset) {
        return ((value[offset + 1] & 0xFF) << 8) 
            |  ((value[offset    ] & 0xFF)     );
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
        return (r(in) << 24) | (r(in) << 16) 
            |  (r(in) <<  8) | (r(in)      );
    }
    
    public static int beb2int(byte[] value) {
        return beb2int(value, 0);
    }
    
    public static int beb2int(byte[] value, int offset) {
        return ((value[offset    ] & 0xFF) << 24) 
            |  ((value[offset + 1] & 0xFF) << 16) 
            |  ((value[offset + 2] & 0xFF) <<  8) 
            |  ((value[offset + 3] & 0xFF)      );
    }
    
    public static int leb2int(InputStream in) throws IOException {
        return (r(in)      ) | (r(in) <<  8) 
            |  (r(in) << 16) | (r(in) << 24);
    }
    
    public static int leb2int(byte[] value) {
        return leb2int(value, 0);
    }
    
    public static int leb2int(byte[] value, int offset) {
        return ((value[offset + 3] & 0xFF) << 24) 
            |  ((value[offset + 2] & 0xFF) << 16) 
            |  ((value[offset + 1] & 0xFF) <<  8) 
            |  ((value[offset    ] & 0xFF)      );
    }
    
    public static long beb2long(InputStream in) throws IOException {
        return (r(in) << 56L) | (r(in) << 48L) 
            |  (r(in) << 40L) | (r(in) << 32L) 
            |  (r(in) << 24L) | (r(in) << 16L) 
            |  (r(in) <<  8L) | (r(in)       );
    }
    
    public static long beb2long(byte[] value) {
        return beb2long(value, 0);
    }
    
    public static long beb2long(byte[] value, int offset) {
        return ((value[offset    ] & 0xFFL) << 56L) 
            |  ((value[offset + 1] & 0xFFL) << 48L) 
            |  ((value[offset + 2] & 0xFFL) << 40L) 
            |  ((value[offset + 3] & 0xFFL) << 32L) 
            |  ((value[offset + 4] & 0xFFL) << 24L) 
            |  ((value[offset + 5] & 0xFFL) << 16L) 
            |  ((value[offset + 6] & 0xFFL) <<  8L) 
            |  ((value[offset + 7] & 0xFFL)       );
    }
    
    public static long leb2long(InputStream in) throws IOException {
        return (r(in)       ) | (r(in) <<  8L) 
            |  (r(in) << 16L) | (r(in) << 24L) 
            |  (r(in) << 32L) | (r(in) << 40L) 
            |  (r(in) << 48L) | (r(in) << 56L);
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
            |  ((value[offset    ] & 0xFFL)       );
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
    
    public static void short2beb(int value, OutputStream out) throws IOException {
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static byte[] short2beb(int value) {
        return short2beb(value, new byte[SHORT]);
    }
    
    public static byte[] short2beb(int value, byte[] dst) {
        return short2beb(value, dst, 0);
    }
    
    public static byte[] short2beb(int value, byte[] dst, int offset) {
        dst[offset    ] = (byte)(value >>> 8);
        dst[offset + 1] = (byte)(value      );
        return dst;
    }
    
    public static void short2leb(int value, OutputStream out) throws IOException {
        out.write(value       );
        out.write(value >>>  8);
    }
    
    public static byte[] short2leb(int value) {
        return short2leb(value, new byte[SHORT]);
    }
    
    public static byte[] short2leb(int value, byte[] dst) {
        return short2leb(value, dst, 0);
    }
    
    public static byte[] short2leb(int value, byte[] dst, int offset) {
        dst[offset + 1] = (byte)(value >>> 8);
        dst[offset    ] = (byte)(value      );
        return dst;
    }
    
    public static void int2beb(int value, OutputStream out) throws IOException {
        out.write(value >>> 24);
        out.write(value >>> 16);
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static byte[] int2beb(int value) {
        return int2beb(value, new byte[INT]);
    }
    
    public static byte[] int2beb(int value, byte[] dst) {
        return int2beb(value, dst, 0);
    }
    
    public static byte[] int2beb(int value, byte[] dst, int offset) {
        dst[offset    ] = (byte)(value >>> 24);
        dst[offset + 1] = (byte)(value >>> 16);
        dst[offset + 2] = (byte)(value >>>  8);
        dst[offset + 3] = (byte)(value       );
        return dst;
    }
    
    public static void int2leb(int value, OutputStream out) throws IOException {
        out.write(value       );
        out.write(value >>>  8);
        out.write(value >>> 16);
        out.write(value >>> 24);
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
        dst[offset    ] = (byte)(value       );
        return dst;
    }
    
    public static void long2beb(long value, OutputStream out) throws IOException {
        out.write((int)(value >>> 56L));
        out.write((int)(value >>> 48L));
        out.write((int)(value >>> 40L));
        out.write((int)(value >>> 32L));
        out.write((int)(value >>> 24L));
        out.write((int)(value >>> 16L));
        out.write((int)(value >>>  8L));
        out.write((int)(value        ));
    }
    
    public static byte[] long2beb(long value) {
        return long2beb(value, new byte[LONG]);
    }
    
    public static byte[] long2beb(long value, byte[] dst) {
        return long2beb(value, dst, 0);
    }
    
    public static byte[] long2beb(long value, byte[] dst, int offset) {
        dst[offset    ] = (byte)(value >>> 56L);
        dst[offset + 1] = (byte)(value >>> 48L);
        dst[offset + 2] = (byte)(value >>> 40L);
        dst[offset + 3] = (byte)(value >>> 32L);
        dst[offset + 4] = (byte)(value >>> 24L);
        dst[offset + 5] = (byte)(value >>> 16L);
        dst[offset + 6] = (byte)(value >>>  8L);
        dst[offset + 7] = (byte)(value        );
        return dst;
    }
    
    public static void long2leb(long value, OutputStream out) throws IOException {
        out.write((int)(value        ));
        out.write((int)(value >>>  8L));
        out.write((int)(value >>> 16L));
        out.write((int)(value >>> 24L));
        out.write((int)(value >>> 32L));
        out.write((int)(value >>> 40L));
        out.write((int)(value >>> 48L));
        out.write((int)(value >>> 56L));
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
        dst[offset    ] = (byte)(value        );
        return dst;
    }
    
    public static void float2beb(float value, OutputStream out) throws IOException {
        int2beb(Float.floatToIntBits(value), out);
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
    
    public static void float2leb(float value, OutputStream out) throws IOException {
        int2leb(Float.floatToIntBits(value), out);
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
    
    public static void double2beb(double value, OutputStream out) throws IOException {
        long2beb(Double.doubleToLongBits(value), out);
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
    
    public static void double2leb(double value, OutputStream out) throws IOException {
        long2leb(Double.doubleToLongBits(value), out);
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
}
