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

package org.ardverk.security.token;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ardverk.net.NetworkUtils;

/**
 * An implementation of {@link DefaultSecurityToken} for {@link SocketAddress}es 
 * and {@link InetAddress}es.
 */
public class AddressSecurityToken extends DefaultSecurityToken {

    private static final boolean USE_PORT = false;
    
    private final boolean usePort;
    
    public AddressSecurityToken(MessageDigest messageDigest, long frequency,
            TimeUnit unit) {
        this(messageDigest, USE_PORT, frequency, unit);
    }
    
    public AddressSecurityToken(MessageDigest messageDigest, boolean usePort, 
            long frequency, TimeUnit unit) {
        super(messageDigest, frequency, unit);
        this.usePort = usePort;
    }

    public AddressSecurityToken(ScheduledExecutorService executor,
            MessageDigest messageDigest, Random random, long frequency,
            TimeUnit unit) {
        this(executor, messageDigest, random, USE_PORT, frequency, unit);
    }
    
    public AddressSecurityToken(ScheduledExecutorService executor,
            MessageDigest messageDigest, Random random, boolean usePort, 
            long frequency, TimeUnit unit) {
        super(executor, messageDigest, random, frequency, unit);
        this.usePort = usePort;
    }
    
    /**
     * Returns {@code true} if port numbers are used to compute security tokens.
     */
    public boolean isUsePort() {
        return usePort;
    }
    
    /**
     * Creates a security token for the given {@link SocketAddress}.
     */
    public byte[] create(SocketAddress address) {
        return create(toBytes(address));
    }
    
    /**
     * Creates a security token for the given {@link InetAddress} and port.
     */
    public byte[] create(InetAddress address, int port) {
        return create(toBytes(address, port));
    }
    
    /**
     * Returns {@code true} if the given security token is valid.
     */
    public boolean isFor(SocketAddress address, byte[] securityToken) {
        return isFor(toBytes(address), securityToken);
    }
    
    /**
     * Returns {@code true} if the given security token is valid.
     */
    public boolean isFor(SocketAddress address, 
            byte[] securityToken, int offset, int length) {
        return isFor(toBytes(address), securityToken, offset, length);
    }
    
    /**
     * Returns {@code true} if the given security token is valid.
     */
    public boolean isFor(InetAddress address, int port, byte[] securityToken) {
        return isFor(toBytes(address, port), securityToken);
    }
    
    /**
     * Returns {@code true} if the given security token is valid.
     */
    public boolean isFor(InetAddress address, int port, 
            byte[] securityToken, int offset, int length) {
        return isFor(toBytes(address, port), securityToken, offset, length);
    }
    
    /**
     * Returns a {@code byte[]} value for the given {@link SocketAddress}.
     */
    private byte[] toBytes(SocketAddress address) {
        if (usePort) {
            return NetworkUtils.toBytes(address);
        }
        
        return NetworkUtils.getAddress(address).getAddress();
    }
    
    /**
     * Returns a {@code byte[]} value for the given {@link InetAddress} and port.
     */
    private byte[] toBytes(InetAddress address, int port) {
        if (usePort) {
            return NetworkUtils.toBytes(address, port);
        }
        
        return address.getAddress();
    }
}
