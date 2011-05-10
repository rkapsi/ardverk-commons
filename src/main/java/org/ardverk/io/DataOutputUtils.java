package org.ardverk.io;

import java.io.IOException;
import java.io.OutputStream;

public class DataOutputUtils {

    private DataOutputUtils() {}
    
    public static void writeShort(OutputStream out, int value) throws IOException {
        out.write((value >>>  8) & 0xFF);
        out.write((value       ) & 0xFF);
    }
    
    public static void writeInt(OutputStream out, int value) throws IOException {
        out.write((value >>> 24) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>>  8) & 0xFF);
        out.write((value       ) & 0xFF);
    }
    
    public static void writeLong(OutputStream out, long value) throws IOException {
        out.write((int)(value >>> 56L) & 0xFF);
        out.write((int)(value >>> 48L) & 0xFF);
        out.write((int)(value >>> 40L) & 0xFF);
        out.write((int)(value >>> 32L) & 0xFF);
        out.write((int)(value >>> 24L) & 0xFF);
        out.write((int)(value >>> 16L) & 0xFF);
        out.write((int)(value >>>  8L) & 0xFF);
        out.write((int)(value        ) & 0xFF);
    }
    
    public static void writeFloat(OutputStream out, float value) throws IOException {
        writeInt(out, Float.floatToIntBits(value));
    }
    
    public static void writeDouble(OutputStream out, double value) throws IOException {
        writeLong(out, Double.doubleToLongBits(value));
    }
}
