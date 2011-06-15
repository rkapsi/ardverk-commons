/*
 * Copyright 2010-2011 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.io;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteUtils {

    private ByteUtils() {}
    
    /**
     * @see DataInput#readFully(byte[])
     */
    public static byte[] readFully(InputStream in, byte[] dst) throws IOException {
        return readFully(in, dst, 0, dst.length);
    }
    
    /**
     * @see DataInput#readFully(byte[])
     */
    public static byte[] readFully(InputStream in, byte[] dst, 
            int offset, int length) throws IOException {
        
        int total = 0;
        while (total < length) {
            int r = in.read(dst, offset + total, length - total);
            if (r == -1) {
                throw new EOFException();
            }
            
            total += r;
        }
        
        return dst;
    }
    
    public static void writeBytes(byte[] data, OutputStream out) throws IOException {
        writeBytes(data, 0, data.length, out);
    }
    
    public static void writeBytes(byte[] data, int offset, int length, 
            OutputStream out) throws IOException {
        DataUtils.int2vbeb(length, out);
        out.write(data, offset, length);
    }
    
    public static byte[] readBytes(InputStream in) throws IOException {
        int length = DataUtils.vbeb2int(in);
        byte[] data = new byte[length];
        return readFully(in, data);
    }
}
