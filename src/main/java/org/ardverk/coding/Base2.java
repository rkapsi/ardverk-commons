/*
 * Copyright 2010 Roger Kapsi
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

package org.ardverk.coding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.ardverk.io.IoUtils;
import org.ardverk.lang.NullArgumentException;

public class Base2 extends AbstractBaseCoder implements Serializable {
    
    private static final long serialVersionUID = 485864896492653810L;

    private static final byte[][] BIN = {
        { '0', '0', '0', '0' }, { '0', '0', '0', '1' }, 
        { '0', '0', '1', '0' }, { '0', '0', '1', '1' }, 
        { '0', '1', '0', '0' }, { '0', '1', '0', '1' },
        { '0', '1', '1', '0' }, { '0', '1', '1', '1' }, 
        { '1', '0', '0', '0' }, { '1', '0', '0', '1' }, 
        { '1', '0', '1', '0' }, { '1', '0', '1', '1' },
        { '1', '1', '0', '0' }, { '1', '1', '0', '1' }, 
        { '1', '1', '1', '0' }, { '1', '1', '1', '1' }
    };
    
    public static final Base2 CODER = new Base2();
    
    private Base2() {}
    
    @Override
    public byte[] encode(byte[] data, int offset, int length) {
        
        if (data == null) {
            throw new NullArgumentException("data");
        }
        
        if (offset < 0 || length < 0 || (offset+length) > data.length) {
            throw new IllegalArgumentException("offset=" + offset + ", length=" 
                    + length + ", data.length=" + data.length);
        }
        
        // For each input byte we will produce four output bytes!
        ByteArrayOutputStream baos 
            = new ByteArrayOutputStream(length * 4);
        
        int end = offset + length;
        byte value = 0;
        
        try {
            for (int i = offset; i < end; i++) {
                value = data[i];
                
                baos.write(BIN[(value >>> 4) & 0xF]);
                baos.write(BIN[(value      ) & 0xF]);
            }
        } catch (IOException err) {
            throw new IllegalStateException("IOException", err);
        } finally {
            IoUtils.close(baos);
        }
        
        return baos.toByteArray();
    }
    
    @Override
    public byte[] decode(byte[] data, int offset, int length) {
        throw new UnsupportedOperationException();
    }
    
    public static byte[] encodeBase2(byte[] data) {
        return CODER.encode(data);
    }
    
    public static byte[] encodeBase2(byte[] data, int offset, int length) {
        return CODER.encode(data, offset, length);
    }
    
    public static byte[] decodeBase2(byte[] data) {
        return CODER.decode(data);
    }
    
    public static byte[] decodeBase2(byte[] data, int offset, int length) {
        return CODER.decode(data, offset, length);
    }
}