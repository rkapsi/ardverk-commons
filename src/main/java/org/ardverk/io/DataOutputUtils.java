package org.ardverk.io;

import java.io.IOException;
import java.io.OutputStream;

public class DataOutputUtils {

    private DataOutputUtils() {}
    
    public static void writeShortBE(OutputStream out, int value) throws IOException {
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static byte[] getShortBE(int value) {
        return getShortBE(value, new byte[2]);
    }
    
    public static byte[] getShortBE(int value, byte[] dst) {
        return getShortBE(value, dst, 0);
    }
    
    public static byte[] getShortBE(int value, byte[] dst, int offset) {
        dst[offset    ] = (byte)(value >>> 8);
        dst[offset + 1] = (byte)(value      );
        return dst;
    }
    
    public static void writeShortLE(OutputStream out, int value) throws IOException {
        out.write(value       );
        out.write(value >>>  8);
    }
    
    public static byte[] getShortLE(int value) {
        return getShortLE(value, new byte[2]);
    }
    
    public static byte[] getShortLE(int value, byte[] dst) {
        return getShortLE(value, dst, 0);
    }
    
    public static byte[] getShortLE(int value, byte[] dst, int offset) {
        dst[offset + 1] = (byte)(value >>> 8);
        dst[offset    ] = (byte)(value      );
        return dst;
    }
    
    public static void writeIntBE(OutputStream out, int value) throws IOException {
        out.write(value >>> 24);
        out.write(value >>> 16);
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static byte[] getIntBE(int value) {
        return getIntBE(value, new byte[4]);
    }
    
    public static byte[] getIntBE(int value, byte[] dst) {
        return getIntBE(value, dst, 0);
    }
    
    public static byte[] getIntBE(int value, byte[] dst, int offset) {
        dst[offset    ] = (byte)(value >>> 24);
        dst[offset + 1] = (byte)(value >>> 16);
        dst[offset + 2] = (byte)(value >>>  8);
        dst[offset + 3] = (byte)(value       );
        return dst;
    }
    
    public static void writeIntLE(OutputStream out, int value) throws IOException {
        out.write(value       );
        out.write(value >>>  8);
        out.write(value >>> 16);
        out.write(value >>> 24);
    }
    
    public static byte[] getIntLE(int value) {
        return getIntLE(value, new byte[4]);
    }
    
    public static byte[] getIntLE(int value, byte[] dst) {
        return getIntLE(value, dst, 0);
    }
    
    public static byte[] getIntLE(int value, byte[] dst, int offset) {
        dst[offset + 3] = (byte)(value >>> 24);
        dst[offset + 2] = (byte)(value >>> 16);
        dst[offset + 1] = (byte)(value >>>  8);
        dst[offset    ] = (byte)(value       );
        return dst;
    }
    
    public static void writeLongBE(OutputStream out, long value) throws IOException {
        out.write((int)(value >>> 56L));
        out.write((int)(value >>> 48L));
        out.write((int)(value >>> 40L));
        out.write((int)(value >>> 32L));
        out.write((int)(value >>> 24L));
        out.write((int)(value >>> 16L));
        out.write((int)(value >>>  8L));
        out.write((int)(value        ));
    }
    
    public static byte[] getLongBE(long value) {
        return getLongBE(value, new byte[8]);
    }
    
    public static byte[] getLongBE(long value, byte[] dst) {
        return getLongBE(value, dst, 0);
    }
    
    public static byte[] getLongBE(long value, byte[] dst, int offset) {
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
    
    public static void writeLongLE(OutputStream out, long value) throws IOException {
        out.write((int)(value        ));
        out.write((int)(value >>>  8L));
        out.write((int)(value >>> 16L));
        out.write((int)(value >>> 24L));
        out.write((int)(value >>> 32L));
        out.write((int)(value >>> 40L));
        out.write((int)(value >>> 48L));
        out.write((int)(value >>> 56L));
    }
    
    public static byte[] getLongLE(long value) {
        return getLongLE(value, new byte[8]);
    }
    
    public static byte[] getLongLE(long value, byte[] dst) {
        return getLongLE(value, dst, 0);
    }
    
    public static byte[] getLongLE(long value, byte[] dst, int offset) {
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
    
    public static void writeFloatBE(OutputStream out, float value) throws IOException {
        writeIntBE(out, Float.floatToIntBits(value));
    }
    
    public static byte[] getFloatBE(float value) {
        return getFloatBE(value, new byte[4]);
    }
    
    public static byte[] getFloatBE(float value, byte[] dst) {
        return getFloatBE(value, dst, 0);
    }
    
    public static byte[] getFloatBE(float value, byte[] dst, int offset) {
        return getIntBE(Float.floatToIntBits(value), dst, offset);
    }
    
    public static void writeFloatLE(OutputStream out, float value) throws IOException {
        writeIntLE(out, Float.floatToIntBits(value));
    }
    
    public static byte[] getFloatLE(float value) {
        return getFloatLE(value, new byte[4]);
    }
    
    public static byte[] getFloatLE(float value, byte[] dst) {
        return getFloatLE(value, dst, 0);
    }
    
    public static byte[] getFloatLE(float value, byte[] dst, int offset) {
        return getIntLE(Float.floatToIntBits(value), dst, offset);
    }
    
    public static void writeDoubleBE(OutputStream out, double value) throws IOException {
        writeLongBE(out, Double.doubleToLongBits(value));
    }
    
    public static byte[] getDoubleBE(double value) {
        return getDoubleBE(value, new byte[8]);
    }
    
    public static byte[] getDoubleBE(double value, byte[] dst) {
        return getDoubleBE(value, dst, 0);
    }
    
    public static byte[] getDoubleBE(double value, byte[] dst, int offset) {
        return getLongBE(Double.doubleToLongBits(value), dst, offset);
    }
    
    public static void writeDoubleLE(OutputStream out, double value) throws IOException {
        writeLongLE(out, Double.doubleToLongBits(value));
    }
    
    public static byte[] getDoubleLE(double value) {
        return getDoubleLE(value, new byte[8]);
    }
    
    public static byte[] getDoubleLE(double value, byte[] dst) {
        return getDoubleLE(value, dst, 0);
    }
    
    public static byte[] getDoubleLE(double value, byte[] dst, int offset) {
        return getLongLE(Double.doubleToLongBits(value), dst, offset);
    }
}
