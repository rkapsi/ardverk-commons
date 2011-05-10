package org.ardverk.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

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
    
    public static int readUnsignedShort(InputStream in) throws IOException {
        return readUnsignedShort(in, ByteOrder.BIG_ENDIAN);
    }
    
    public static int readUnsignedShort(InputStream in, ByteOrder bo) throws IOException {
        if (bo.equals(ByteOrder.BIG_ENDIAN)) {
            return (r(in) << 8) | r(in);
        }
        return r(in) | (r(in) << 8);
    }
    
    public static short readShort(InputStream in) throws IOException {
        return (short)readUnsignedShort(in);
    }
    
    public static short readShort(InputStream in, ByteOrder bo) throws IOException {
        return (short)readUnsignedShort(in, bo);
    }
    
    public static char readChar(InputStream in) throws IOException {
        return (char)readUnsignedShort(in);
    }
    
    public static char readChar(InputStream in, ByteOrder bo) throws IOException {
        return (char)readUnsignedShort(in, bo);
    }
    
    public static int readInt(InputStream in) throws IOException {
        return readInt(in, ByteOrder.BIG_ENDIAN);
    }
    
    public static int readInt(InputStream in, ByteOrder bo) throws IOException {
        if (bo.equals(ByteOrder.BIG_ENDIAN)) {
            return (r(in) << 24) | (r(in) << 16) 
                |  (r(in) <<  8) | (r(in)      );
        }
        
        return (r(in)      ) | (r(in) <<  8) 
            |  (r(in) << 16) | (r(in) << 24);
    }
    
    public static long readLong(InputStream in) throws IOException {
        return readLong(in, ByteOrder.BIG_ENDIAN);
    }
    
    public static long readLong(InputStream in, ByteOrder bo) throws IOException {
        if (bo.equals(ByteOrder.BIG_ENDIAN)) {
            return (r(in) << 56L) | (r(in) << 48L) 
                |  (r(in) << 40L) | (r(in) << 32L) 
                |  (r(in) << 24L) | (r(in) << 16L) 
                |  (r(in) <<  8L) | (r(in)       );
        }
        
        return (r(in)       ) | (r(in) <<  8L) 
            |  (r(in) << 16L) | (r(in) << 24L) 
            |  (r(in) << 32L) | (r(in) << 40L) 
            |  (r(in) << 48L) | (r(in) << 56L);
    }
    
    public static float readFloat(InputStream in) throws IOException {
        return Float.intBitsToFloat(readInt(in));
    }
    
    public static double readDouble(InputStream in) throws IOException {
        return Double.longBitsToDouble(readLong(in));
    }
}
