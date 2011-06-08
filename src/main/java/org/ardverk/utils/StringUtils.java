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

package org.ardverk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.ardverk.io.ByteUtils;

public class StringUtils {

    public static final String UTF_8 = "UTF-8";
    
    public static final String ISO_8859_1 = "ISO-8859-1";
    
    private StringUtils() {}
    
    public static String toString(byte[] data) {
        return toString(data, 0, data.length);
    }
    
    public static String toString(byte[] data, int offset, int length) {
        return toString(data, offset, length, UTF_8);
    }
    
    public static String toString(byte[] data, String encoding) {
        return toString(data, 0, data.length, encoding);
    }
    
    public static String toString(byte[] data, int offset, int length, String encoding) {
        try {
            return new String(data, offset, length, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("encoding=" + encoding, e);
        }
    }
    
    public static byte[] getBytes(String data) {
        return getBytes(data, UTF_8);
    }
    
    public static byte[] getBytes(String data, String encoding) {
        try {
            return data.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("encoding=" + encoding, e);
        }
    }
    
    /**
     * Writes the {@link String} to the given {@link OutputStream}.
     */
    public static void writeString(String value, OutputStream out) throws IOException {
        writeString(value, UTF_8, out);
    }
    
    /**
     * Writes the {@link String} to the given {@link OutputStream}.
     */
    public static void writeString(String value, String encoding, OutputStream out) throws IOException {
        byte[] data = getBytes(value, encoding);
        ByteUtils.writeBytes(data, out);
    }
    
    /**
     * Reads and returns a {@link String} from the given {@link InputStream}.
     */
    public static String readString(InputStream in) throws IOException {
        return readString(in, UTF_8);
    }
    
    /**
     * Reads and returns a {@link String} from the given {@link InputStream}.
     */
    public static String readString(InputStream in, String encoding) throws IOException {
        byte[] data = ByteUtils.readBytes(in);
        return toString(data, encoding);
    }

    /**
     * Returns true if the given {@link String} is {@code null}, is an
     * empty string or is just a bunch of whitespace characters as defined
     * by {@link Character#isWhitespace(char)}.
     */
    public static boolean isEmpty(String data) {
        if (data != null && !data.isEmpty()) {
            for (int i = data.length()-1; i >= 0; --i) {
                if (!Character.isWhitespace(data.charAt(i))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public static String trim(String value, char ch, char... others) {
        if (value != null) {
            value = trim(value, ch);
            
            if (others != null) {
                for (int i = others.length-1; i >= 0; --i) {
                    value = trim(value, others[i]);
                }
            }
        }
        
        return value;
    }
    
    public static String trim(String value, char ch) {
        if (value != null) {
            int length = value.length();
            
            int p = 0;
            int q = length;
            
            for (int i = 0; i < length; i++) {
                if (value.charAt(i) != ch) {
                    break;
                }
                
                ++p;
            }
            
            for (int i = length-1; p < i; --i) {
                if (value.charAt(i) != ch) {
                    break;
                }
                
                --q;
            }
            
            if (p != 0 || q != length) {
                return value.substring(p, q);
            }
        }
        
        return value;
    }
}