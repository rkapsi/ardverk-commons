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

/**
 * An interface to unify {@link MessageDigest} and {@link Mac}.
 * 
 * @see HashUtils
 */
public interface Hash {

    /**
     * @see MessageDigest#getAlgorithm()
     * @see Mac#getAlgorithm()
     */
    public String getAlgorithm();
    
    /**
     * @see MessageDigest#getDigestLength()
     * @see Mac#getMacLength()
     */
    public int getLength();
    
    /**
     * @see MessageDigest#reset()
     * @see Mac#reset()
     */
    public void reset();
    
    /**
     * @see MessageDigest#update(byte)
     * @see Mac#update(byte)
     */
    public void update(byte input);
    
    /**
     * @see MessageDigest#update(byte[])
     * @see Mac#update(byte[])
     */
    public void update(byte[] input);
    
    /**
     * @see MessageDigest#update(byte[], int, int)
     * @see Mac#update(byte[], int, int)
     */
    public void update(byte[] input, int offset, int length);
    
    /**
     * @see MessageDigest#digest()
     * @see Mac#doFinal()
     */
    public byte[] doFinal();
}
