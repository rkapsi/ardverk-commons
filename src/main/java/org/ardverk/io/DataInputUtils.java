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
    
    public static int toUnsignedShortBE(byte[] value) throws IOException {
        return toUnsignedShortBE(value, 0);
    }
    
    public static int toUnsignedShortBE(byte[] value, int offset) throws IOException {
        return ((value[offset    ] & 0xFF) << 8) 
            |  ((value[offset + 1] & 0xFF)     );
    }
    
    public static int readUnsignedShortLE(InputStream in) throws IOException {
        return r(in) | (r(in) << 8);
    }
    
    public static int toUnsignedShortLE(byte[] value) throws IOException {
        return toUnsignedShortLE(value, 0);
    }
    
    public static int toUnsignedShortLE(byte[] value, int offset) throws IOException {
        return ((value[offset + 1] & 0xFF) << 8) 
            |  ((value[offset    ] & 0xFF)     );
    }
    
    public static short readShortBE(InputStream in) throws IOException {
        return (short)readUnsignedShortBE(in);
    }
    
    public static int toShortBE(byte[] value) throws IOException {
        return (short)toUnsignedShortBE(value);
    }
    
    public static int toShortBE(byte[] value, int offset) throws IOException {
        return (short)toUnsignedShortBE(value, offset);
    }
    
    public static short readShortLE(InputStream in) throws IOException {
        return (short)readUnsignedShortLE(in);
    }
    
    public static int toShortLE(byte[] value) throws IOException {
        return (short)toUnsignedShortLE(value);
    }
    
    public static int toShortLE(byte[] value, int offset) throws IOException {
        return (short)toUnsignedShortLE(value, offset);
    }
    
    public static int readIntBE(InputStream in) throws IOException {
        return (r(in) << 24) | (r(in) << 16) 
            |  (r(in) <<  8) | (r(in)      );
    }
    
    public static int toIntBE(byte[] value) throws IOException {
        return toIntBE(value, 0);
    }
    
    public static int toIntBE(byte[] value, int offset) throws IOException {
        return ((value[offset    ] & 0xFF) << 24) 
            |  ((value[offset + 1] & 0xFF) << 16) 
            |  ((value[offset + 2] & 0xFF) <<  8) 
            |  ((value[offset + 3] & 0xFF)      );
    }
    
    public static int readIntLE(InputStream in) throws IOException {
        return (r(in)      ) | (r(in) <<  8) 
            |  (r(in) << 16) | (r(in) << 24);
    }
    
    public static int toIntLE(byte[] value) throws IOException {
        return toIntLE(value, 0);
    }
    
    public static int toIntLE(byte[] value, int offset) throws IOException {
        return ((value[offset + 3] & 0xFF) << 24) 
            |  ((value[offset + 2] & 0xFF) << 16) 
            |  ((value[offset + 1] & 0xFF) <<  8) 
            |  ((value[offset    ] & 0xFF)      );
    }
    
    public static long readLongBE(InputStream in) throws IOException {
        return (r(in) << 56L) | (r(in) << 48L) 
            |  (r(in) << 40L) | (r(in) << 32L) 
            |  (r(in) << 24L) | (r(in) << 16L) 
            |  (r(in) <<  8L) | (r(in)       );
    }
    
    public static long toLongBE(byte[] value) throws IOException {
        return toLongBE(value, 0);
    }
    
    public static long toLongBE(byte[] value, int offset) throws IOException {
        return ((value[offset    ] & 0xFF) << 56L) 
            |  ((value[offset + 1] & 0xFF) << 48L) 
            |  ((value[offset + 2] & 0xFF) << 40L) 
            |  ((value[offset + 3] & 0xFF) << 32L) 
            |  ((value[offset + 4] & 0xFF) << 24L) 
            |  ((value[offset + 5] & 0xFF) << 16L) 
            |  ((value[offset + 6] & 0xFF) <<  8L) 
            |  ((value[offset + 7] & 0xFF)       );
    }
    
    public static long readLongLE(InputStream in) throws IOException {
        return (r(in)       ) | (r(in) <<  8L) 
            |  (r(in) << 16L) | (r(in) << 24L) 
            |  (r(in) << 32L) | (r(in) << 40L) 
            |  (r(in) << 48L) | (r(in) << 56L);
    }
    
    public static long toLongLE(byte[] value) throws IOException {
        return toLongLE(value, 0);
    }
    
    public static long toLongLE(byte[] value, int offset) throws IOException {
        return ((value[offset + 7] & 0xFF) << 56L) 
            |  ((value[offset + 6] & 0xFF) << 48L) 
            |  ((value[offset + 5] & 0xFF) << 40L) 
            |  ((value[offset + 4] & 0xFF) << 32L) 
            |  ((value[offset + 3] & 0xFF) << 24L) 
            |  ((value[offset + 2] & 0xFF) << 16L) 
            |  ((value[offset + 1] & 0xFF) <<  8L) 
            |  ((value[offset    ] & 0xFF)       );
    }
    
    public static float readFloatBE(InputStream in) throws IOException {
        return Float.intBitsToFloat(readIntBE(in));
    }
    
    public static float toFloatBE(byte[] value) throws IOException {
        return toFloatBE(value, 0);
    }
    
    public static float toFloatBE(byte[] value, int offset) throws IOException {
        return Float.intBitsToFloat(toIntBE(value, offset));
    }
    
    public static float readFloatLE(InputStream in) throws IOException {
        return Float.intBitsToFloat(readIntLE(in));
    }
    
    public static float toFloatLE(byte[] value) throws IOException {
        return toFloatLE(value, 0);
    }
    
    public static float toFloatLE(byte[] value, int offset) throws IOException {
        return Float.intBitsToFloat(toIntLE(value, offset));
    }
    
    public static double readDoubleBE(InputStream in) throws IOException {
        return Double.longBitsToDouble(readLongBE(in));
    }
    
    public static double toDoubleBE(byte[] value) throws IOException {
        return toDoubleBE(value, 0);
    }
    
    public static double toDoubleBE(byte[] value, int offset) throws IOException {
        return Double.longBitsToDouble(toLongBE(value, offset));
    }
    
    public static double readDoubleLE(InputStream in) throws IOException {
        return Double.longBitsToDouble(readLongLE(in));
    }
    
    public static double toDoubleLE(byte[] value) throws IOException {
        return toDoubleLE(value, 0);
    }
    
    public static double toDoubleLE(byte[] value, int offset) throws IOException {
        return Double.longBitsToDouble(toLongLE(value, offset));
    }
}
