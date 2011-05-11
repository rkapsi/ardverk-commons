package org.ardverk.io;

import java.io.IOException;
import java.io.OutputStream;

import org.ardverk.lang.Bytes;

public class DataOutputUtils {

    private DataOutputUtils() {}
    
    public static void short2beb(int value, OutputStream out) throws IOException {
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static byte[] short2beb(int value) {
        return short2beb(value, new byte[Bytes.SHORT]);
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
        return short2leb(value, new byte[Bytes.SHORT]);
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
        return int2beb(value, new byte[Bytes.INT]);
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
        return int2leb(value, new byte[Bytes.INT]);
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
        return long2beb(value, new byte[Bytes.LONG]);
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
        return long2leb(value, new byte[Bytes.LONG]);
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
        return float2beb(value, new byte[Bytes.FLOAT]);
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
        return float2leb(value, new byte[Bytes.FLOAT]);
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
        return double2beb(value, new byte[Bytes.DOUBLE]);
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
        return double2leb(value, new byte[Bytes.DOUBLE]);
    }
    
    public static byte[] double2leb(double value, byte[] dst) {
        return double2leb(value, dst, 0);
    }
    
    public static byte[] double2leb(double value, byte[] dst, int offset) {
        return long2leb(Double.doubleToLongBits(value), dst, offset);
    }
}
