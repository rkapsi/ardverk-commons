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

import java.security.MessageDigest;

import javax.crypto.Mac;

import org.ardverk.lang.NullArgumentException;

public class HashUtils {

    private HashUtils() {}
    
    /**
     * Returns a {@link Hash} for the given {@link MessageDigest}.
     */
    public static Hash wrap(final MessageDigest md) {
        if (md == null) {
            throw new NullArgumentException("md");
        }
        
        if (md instanceof Hash) {
            return (Hash)md;
        }
        
        return new Hash() {

            @Override
            public String getAlgorithm() {
                return md.getAlgorithm();
            }

            @Override
            public int getLength() {
                return md.getDigestLength();
            }

            @Override
            public void reset() {
                md.reset();
            }

            @Override
            public void update(byte input) {
                md.update(input);
            }

            @Override
            public void update(byte[] input) {
                md.update(input);
            }

            @Override
            public void update(byte[] input, int offset, int length) {
                md.update(input, offset, length);
            }

            @Override
            public byte[] doFinal() {
                return md.digest();
            }
        };
    }
    
    /**
     * Returns a {@link Hash} for the given {@link Mac}.
     */
    public static Hash wrap(final Mac mac) {
        if (mac == null) {
            throw new NullArgumentException("mac");
        }
        
        if (mac instanceof Hash) {
            return (Hash)mac;
        }
        
        return new Hash() {

            @Override
            public String getAlgorithm() {
                return mac.getAlgorithm();
            }

            @Override
            public int getLength() {
                return mac.getMacLength();
            }

            @Override
            public void reset() {
                mac.reset();
            }

            @Override
            public void update(byte input) {
                mac.update(input);
            }

            @Override
            public void update(byte[] input) {
                mac.update(input);
            }

            @Override
            public void update(byte[] input, int offset, int length) {
                mac.update(input, offset, length);
            }

            @Override
            public byte[] doFinal() {
                return mac.doFinal();
            }
        };
    }
}
