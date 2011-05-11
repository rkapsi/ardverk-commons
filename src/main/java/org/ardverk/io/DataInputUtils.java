package org.ardverk.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DataInputUtils {

    private DataInputUtils() {}
    
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
}
