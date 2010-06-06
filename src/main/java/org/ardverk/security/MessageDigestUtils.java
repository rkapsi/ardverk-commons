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

package org.ardverk.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class for {@link MessageDigest}.
 */
public class MessageDigestUtils {
    
    public static final String SHA1 = "SHA1";
    
    public static final String MD5 = "MD5";
    
    private MessageDigestUtils() {}
    
    /**
     * Creates and returns a {@link MessageDigest} for SHA1
     */
    public static MessageDigest createSHA1() {
        return create(SHA1);
    }
    
    /**
     * Creates and returns a {@link MessageDigest} for MD5
     */
    public static MessageDigest createMD5() {
        return create(MD5);
    }
    
    /**
     * Creates and returns a {@link MessageDigest} for the given algorithm.
     */
    public static MessageDigest create(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(
                    "algorithm=" + algorithm, e);
        }
    }
}
