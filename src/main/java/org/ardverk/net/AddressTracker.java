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

package org.ardverk.net;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import org.ardverk.collection.FixedSizeHashSet;
import org.ardverk.lang.NullArgumentException;

/**
 * The {@link AddressTracker} helps us to determinate our external/public 
 * {@link InetAddress} in a distributed but possibly NAT'ed environment.
 */
public class AddressTracker {

    /**
     * The {@link NetworkMask} makes sure that consecutive {@code set}
     * calls from the same clients are being ignored.
     */
    private final NetworkMask mask;
    
    // We use a ByteBuffer because it implements equals() and hashCode()
    private final FixedSizeHashSet<ByteBuffer> history;
    
    /**
     * The current {@link InetAddress}
     */
    private InetAddress current = null;
    
    /**
     * The temporary {@link InetAddress}
     */
    private InetAddress temporary = null;
    
    /**
     * Creates an {@link AddressTracker}
     */
    public AddressTracker(NetworkMask mask, int count) {
        this (null, mask, count);
    }
    
    /**
     * Creates an {@link AddressTracker}
     */
    public AddressTracker(InetAddress address, NetworkMask mask, int count) {
        if (mask == null) {
            throw new NullArgumentException("mask");
        }
        
        if (count < 0) {
            throw new IllegalArgumentException("count=" + count);
        }
        
        this.current = address;
        this.mask = mask;
        this.history = new FixedSizeHashSet<ByteBuffer>(count);
    }
    
    /**
     * Sets the current {@link InetAddress}.
     */
    public synchronized void set(SocketAddress address) {
        set(NetworkUtils.getAddress(address));
    }
    
    /**
     * Sets the current {@link InetAddress}.
     */
    public synchronized void set(InetAddress address) {
        this.current = address;
        
        temporary = null;
        history.clear();
    }
    
    /**
     * Sets the current {@link InetAddress}.
     */
    public synchronized boolean set(SocketAddress src, SocketAddress address) {
        return set(NetworkUtils.getAddress(src), 
                NetworkUtils.getAddress(address));
    }
    
    /**
     * Sets the current {@link InetAddress}.
     */
    public synchronized boolean set(InetAddress src, SocketAddress address) {
        return set(src, NetworkUtils.getAddress(address));
    }
    
    /**
     * Sets the current {@link InetAddress}.
     */
    public synchronized boolean set(SocketAddress src, InetAddress address) {
        return set(NetworkUtils.getAddress(src), address);
    }
    
    /**
     * Sets the current {@link InetAddress}.
     * 
     * @param src The source of the {@code address}
     * @param address Our {@link InetAddress}
     */
    public synchronized boolean set(InetAddress src, InetAddress address) {
        if (src == null) {
            throw new NullArgumentException("src");
        }
        
        if (address == null) {
            throw new NullArgumentException("address");
        }
        
        // Do nothing if both addresses are equal
        if (current != null && current.equals(address)) {
            return true;
        }
        
        // Initialize the temporary address with the given value if it's null
        if (temporary == null) {
            temporary = address;
            
        // Reset the temporary address if it doesn't match with the given 
        // address and continue working with the one we already have
        } else if (!temporary.equals(address)) {
            temporary = null;
            history.clear();
            return false;
        }
        
        ByteBuffer network = ByteBuffer.wrap(mask.mask(src));
        
        // Make sure we're not accepting proposals more than once from the 
        // same Network during the discovery process
        if (!history.contains(network)) {
            history.add(network);
            if (history.isFull()) {
                current = temporary;
                temporary = null;
                history.clear();
            }
            
            return true;
        } 
        
        return false;
    }
    
    /**
     * Returns the current {@link InetAddress} or {@code null} if it's 
     * not known yet.
     */
    public synchronized InetAddress get() {
        return current;
    }
}