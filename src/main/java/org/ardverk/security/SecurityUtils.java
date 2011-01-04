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

package org.ardverk.security;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;

import org.ardverk.io.IoUtils;

public class SecurityUtils {

    private static final int SEED_LENGTH = 32;
    
    private SecurityUtils() {}
    
    /**
     * Creates and returns a {@link SecureRandom}. The difference between
     * this factory method and {@link SecureRandom}'s default constructor
     * is that this will try to initialize the {@link SecureRandom} with
     * an initial seed from /dev/urandom while the default constructor will
     * attempt to do the same from /dev/random which may block if there is
     * not enough data available.
     */
    public static SecureRandom createSecureRandom() {
        // All Unix like Systems should have this device.
        File file = new File("/dev/urandom");
        
        DataInputStream in = null;
        try {
            if (file.exists() && file.canRead()) {
                in = new DataInputStream(new FileInputStream(file));
                
                byte[] seed = new byte[SEED_LENGTH];
                in.readFully(seed);
                return new SecureRandom(seed);
            }
        } catch (SecurityException ignore) {
        } catch (IOException ignore) {
        } finally {
            IoUtils.close(in);
        }
        
        // We're either on Windows or something else happened.
        return new SecureRandom();
    }
}