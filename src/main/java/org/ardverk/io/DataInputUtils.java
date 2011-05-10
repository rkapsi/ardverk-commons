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

    public static boolean readBoolean(InputStream in) throws IOException {
        return r(in) != 0;
    }
    
    public static int readUnsignedByte(InputStream in) throws IOException {
        return r(in);
    }
    
    public static byte readByte(InputStream in) throws IOException {
        return (byte)readUnsignedByte(in);
    }
    
    public static int readUnsignedShortBE(InputStream in) throws IOException {
        return (r(in) << 8) | r(in);
    }
    
    public static int readUnsignedShortLE(InputStream in) throws IOException {
        return r(in) | (r(in) << 8);
    }
    
    public static short readShortBE(InputStream in) throws IOException {
        return (short)readUnsignedShortBE(in);
    }
    
    public static short readShortLE(InputStream in) throws IOException {
        return (short)readUnsignedShortLE(in);
    }
    
    public static int readIntBE(InputStream in) throws IOException {
        return (r(in) << 24) | (r(in) << 16) 
            |  (r(in) <<  8) | (r(in)      );
    }
    
    public static int readIntLE(InputStream in) throws IOException {
        return (r(in)      ) | (r(in) <<  8) 
            |  (r(in) << 16) | (r(in) << 24);
    }
    
    public static long readLongBE(InputStream in) throws IOException {
        return (r(in) << 56L) | (r(in) << 48L) 
            |  (r(in) << 40L) | (r(in) << 32L) 
            |  (r(in) << 24L) | (r(in) << 16L) 
            |  (r(in) <<  8L) | (r(in)       );
    }
    
    public static long readLongLE(InputStream in) throws IOException {
        return (r(in)       ) | (r(in) <<  8L) 
            |  (r(in) << 16L) | (r(in) << 24L) 
            |  (r(in) << 32L) | (r(in) << 40L) 
            |  (r(in) << 48L) | (r(in) << 56L);
    }
    
    public static float readFloatBE(InputStream in) throws IOException {
        return Float.intBitsToFloat(readIntBE(in));
    }
    
    public static float readFloatLE(InputStream in) throws IOException {
        return Float.intBitsToFloat(readIntLE(in));
    }
    
    public static double readDoubleBE(InputStream in) throws IOException {
        return Double.longBitsToDouble(readLongBE(in));
    }
    
    public static double readDoubleLE(InputStream in) throws IOException {
        return Double.longBitsToDouble(readLongLE(in));
    }
}
