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

import java.io.UnsupportedEncodingException;

public class StringUtils {

    public static final String UTF_8 = "UTF-8";
    
    public static final String ISO_8859_1 = "ISO-8859-1";
    
    private StringUtils() {}
    
    public static String toString(byte[] data) {
        return toString(data, UTF_8);
    }
    
    public static String toString(byte[] data, String encoding) {
        try {
            return new String(data, encoding);
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
}