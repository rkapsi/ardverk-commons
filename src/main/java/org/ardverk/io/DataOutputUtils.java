package org.ardverk.io;

import java.io.IOException;
import java.io.OutputStream;

public class DataOutputUtils {

    private DataOutputUtils() {}
    
    public static void writeShortBE(OutputStream out, int value) throws IOException {
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static void writeShortLE(OutputStream out, int value) throws IOException {
        out.write(value       );
        out.write(value >>>  8);
    }
    
    public static void writeIntBE(OutputStream out, int value) throws IOException {
        out.write(value >>> 24);
        out.write(value >>> 16);
        out.write(value >>>  8);
        out.write(value       );
    }
    
    public static void writeIntLE(OutputStream out, int value) throws IOException {
        out.write(value       );
        out.write(value >>>  8);
        out.write(value >>> 16);
        out.write(value >>> 24);
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
    
    public static void writeFloatBE(OutputStream out, float value) throws IOException {
        writeIntBE(out, Float.floatToIntBits(value));
    }
    
    public static void writeFloatLE(OutputStream out, float value) throws IOException {
        writeIntLE(out, Float.floatToIntBits(value));
    }
    
    public static void writeDoubleBE(OutputStream out, double value) throws IOException {
        writeLongBE(out, Double.doubleToLongBits(value));
    }
    
    public static void writeDoubleLE(OutputStream out, double value) throws IOException {
        writeLongLE(out, Double.doubleToLongBits(value));
    }
}
