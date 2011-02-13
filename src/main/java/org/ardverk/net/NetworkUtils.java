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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class NetworkUtils {

    private NetworkUtils() {}
    
    /**
     * Creates and returns an unresolved {@link InetSocketAddress}.
     * 
     * @see InetSocketAddress#createUnresolved(String, int).
     */
    public static InetSocketAddress createUnresolved(String host, int port) {
        return InetSocketAddress.createUnresolved(host, port);
    }
    
    /**
     * Creates and returns a resolved {@link InetSocketAddress}.
     */
    public static InetSocketAddress createResolved(String host, int port) {
        return new InetSocketAddress(host, port);
    }
    
    /**
     * Creates and returns a resolved {@link InetSocketAddress}.
     */
    public static InetSocketAddress createResolved(InetAddress address, int port) {
        return new InetSocketAddress(address, port);
    }
    
    /**
     * Creates and returns an {@link InetSocketAddress} where the IP-address
     * is the wildcard address and the port number is the specified value.
     */
    public static InetSocketAddress createAny(int port) {
        if (!isValidPort(port)) {
            throw new IllegalArgumentException("port=" + port);
        }
        
        return new InetSocketAddress(port);
    }
    
    /**
     * Resolves (if necessary) the given {@link SocketAddress} and returns it.
     */
    public static InetSocketAddress getResolved(SocketAddress address) {
        InetSocketAddress isa = (InetSocketAddress)address;
        if (isa.isUnresolved()) {
            return new InetSocketAddress(isa.getHostName(), isa.getPort());
        }
        
        return isa;
    }
    
    /**
     * An utility method to get the {@link InetAddress} from the
     * {@link SocketAddress}.
     */
    public static InetAddress getAddress(SocketAddress address) {
        return getResolved(address).getAddress();
    }
    
    /**
     * An utility method to get the {@link InetSocketAddress#getHostName()}.
     */
    public static String getHostName(SocketAddress address) {
        return ((InetSocketAddress)address).getHostName();
    }
    
    /**
     * Returns the port number.
     */
    public static int getPort(SocketAddress address) {
        return ((InetSocketAddress)address).getPort();
    }
    
    /**
     * Returns {@code true} if the given {@link SocketAddress} is unresolved.
     */
    public static boolean isUnresolved(SocketAddress address) {
        return ((InetSocketAddress)address).isUnresolved();
    }
    
    /**
     * Returns {@code true} if both {@link SocketAddress}es have 
     * the same {@link InetAddress}
     */
    public static boolean isSameAddress(SocketAddress a1, SocketAddress a2) {
        if (isUnresolved(a1) && isUnresolved(a2)) {
            if (getHostName(a1).equals(getHostName(a2))) {
                return true;
            }
        }
        
        return getResolved(a1).getAddress().equals(
                getResolved(a2).getAddress());
    }
    
    /**
     * Returns {@code true} if the given port is valid. A valid (usable) 
     * port number must be greater than zero and less than 0xFFFF.
     */
    public static boolean isValidPort(int port) {
        return PortRange.isUsable(port);
    }
    
    /**
     * Returns {@code true} if the given {@link SocketAddress} has a valid port 
     * number. A valid (usable) port number must be greater than zero and 
     * less than 0xFFFF.
     */
    public static boolean isValidPort(SocketAddress address) {
        return isValidPort(getPort(address));
    }
    
    /**
     * Returns {@code true} if the given {@link SocketAddress} is a 
     * non-publicly routable IP-Address. 
     */
    public static boolean isPrivateAddress(SocketAddress address) {
        return isPrivateAddress(getAddress(address));
    }
    
    /**
     * Returns {@code true} if the given {@link InetAddress} is a non-publicly
     * routable IP-Address. 
     */
    public static boolean isPrivateAddress(InetAddress address) {
        return isPrivateAddress(address.getAddress());
    }
    
    /**
     * Returns {@code true} if the given address is a non-publicly
     * routable IP-Address. 
     */
    public static boolean isPrivateAddress(byte[] address) {
        if (isAnyLocalAddress(address) 
                || isLoopbackAddress(address) 
                || isDocumentationAddress(address)
                || isLinkLocalAddress(address) 
                || isSiteLocalAddress(address)
                || isBroadcastAddress(address)
                || isUniqueLocalUnicastAddress(address)
                || isInvalidAddress(address)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * @see InetAddress#isAnyLocalAddress()
     */
    public static boolean isAnyLocalAddress(byte[] address) {
        for (int i = 0; i < address.length; i++) {
            if (address[i] != 0x00) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * @see InetAddress#isLoopbackAddress()
     */
    public static boolean isLoopbackAddress(byte[] address) {
        // 127.x.x.x
        if (isClassicAddress(address)) {
            return (address[0] & 0xFF) == 127;
        }
        
        // ::01
        if (address[15] != 0x01) {
            return false;
        }
        
        for (int i = 0; i < 15; i++) {
            if (address[i] != 0x00) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * @see InetAddress#isLinkLocalAddress()
     */
    public static boolean isLinkLocalAddress(byte[] address) {
        if (isClassicAddress(address)) {
            // link-local unicast in IPv4 (169.254.0.0/16)
            // defined in "Documenting Special Use IPv4 Address Blocks
            // that have been Registered with IANA" by Bill Manning
            // draft-manning-dsua-06.txt
            return ((address[0] & 0xFF) == 169) 
                && ((address[1] & 0xFF) == 254);
        }
        
        return ((address[0] & 0xFF) == 0xFE 
                && (address[1] & 0xC0) == 0x80);
    }
    
    /**
     * @see InetAddress#isSiteLocalAddress()
     */
    public static boolean isSiteLocalAddress(byte[] address) {
        if (isClassicAddress(address)) {
            // refer to RFC 1918
            // 10/8 prefix
            // 172.16/12 prefix
            // 192.168/16 prefix
            return ((address[0] & 0xFF) == 10)
                || (((address[0] & 0xFF) == 172) 
                        && ((address[1] & 0xF0) == 16))
                || (((address[0] & 0xFF) == 192) 
                        && ((address[1] & 0xFF) == 168));
        }
        
        return ((address[0] & 0xFF) == 0xFE 
                && (address[1] & 0xC0) == 0xC0);
    }
    
    /**
     * An IPv6 address for non-Internet connected devices.
     * 
     * <p>http://tools.ietf.org/html/rfc4193
     * <p>http://en.wikipedia.org/wiki/Unique_local_address
     */
    public static boolean isUniqueLocalUnicastAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            return isUniqueLocalUnicastAddress(address.getAddress());
        }
        return false;
    }
    
    /**
     * An IPv6 address for non-Internet connected devices.
     * 
     * <p>http://tools.ietf.org/html/rfc4193
     * <p>http://en.wikipedia.org/wiki/Unique_local_address
     */
    public static boolean isUniqueLocalUnicastAddress(byte[] address) {
        // FC00::/7
        if (address.length == 16) {
            return (address[0] & 0xFE) == 0xFC;
        }
        return false;
    }

    /**
     * A IPv4 Broadcast Address
     * 
     * <p>http://tools.ietf.org/html/rfc919
     */
    public static boolean isBroadcastAddress(InetAddress address) {
        return isBroadcastAddress(address.getAddress());
    }
    
    /**
     * A IPv4 Broadcast Address
     * 
     * <p>http://tools.ietf.org/html/rfc919
     */
    public static boolean isBroadcastAddress(byte[] address) {
        if (isClassicAddress(address) || isClassicMappedAddress(address)) {
            return (address[/* 0 */ address.length - 4] & 0xFF) == 0xFF;
        }
        
        return false;
    }

    /**
     * Returns true if the given {@link InetAddress} is invalid.
     */
    public static boolean isInvalidAddress(InetAddress address) {
        return isInvalidAddress(address.getAddress());
    }
    
    /**
     * Returns true if the given {@link InetAddress} is invalid.
     */
    public static boolean isInvalidAddress(byte[] address) {
        // 0.0.0.0 is considered invalid
        if (isClassicAddress(address) || isClassicMappedAddress(address)) {
            return address[/* 0 */ address.length - 4] == 0x00;
        }
        return false;
    }
    
    /**
     * An IPv6 documentation address.
     * 
     * <p>http://tools.ietf.org/html/rfc3849
     */
    public static boolean isDocumentationAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            return isDocumentationAddress(address.getAddress());
        }
        return false;
    }
    
    /**
     * An IPv6 documentation address.
     * 
     * <p>http://tools.ietf.org/html/rfc3849
     */
    public static boolean isDocumentationAddress(byte[] address) {
        // 2001:0DB8::/32
        if (address.length == 16) {
            return (address[0] & 0xFF) == 0x20
                && (address[1] & 0xFF) == 0x01
                && (address[2] & 0xFF) == 0x0D
                && (address[3] & 0xFF) == 0xB8;
        }
        return false;
    }
    
    /**
     * Returns true if the given {@link InetAddress} is an IPv4 address
     */
    public static boolean isClassicAddress(InetAddress address) {
        return address instanceof Inet4Address;
    }
    
    /**
     * Returns true if the given address is an IPv4 address
     */
    public static boolean isClassicAddress(byte[] address) {
        return address.length == 4;
    }

    /**
     * Returns true if the given {@link InetAddress} is an IPv4 address
     * that is embedded into a IPv6 frame. In other words if it's an IPv6
     * address in IPv4 address space.
     */
    public static boolean isClassicMappedAddress(InetAddress address) {
        return isClassicMappedAddress(address.getAddress());
    }
    
    /**
     * Returns true if the given {@link InetAddress} is an IPv4 address
     * that is embedded into a IPv6 frame. In other words if it's an IPv6
     * address in IPv4 address space.
     */
    public static boolean isClassicMappedAddress(byte[] address) {
        if (address.length == 16 
                && (address[ 0] == 0x00) && (address[ 1] == 0x00) 
                && (address[ 2] == 0x00) && (address[ 3] == 0x00) 
                && (address[ 4] == 0x00) && (address[ 5] == 0x00) 
                && (address[ 6] == 0x00) && (address[ 7] == 0x00) 
                && (address[ 8] == 0x00) && (address[ 9] == 0x00) 
                && (address[10] == (byte)0xFF) && (address[11] == (byte)0xFF)) {   
            return true;
        }
        
        return false;  
    }
    
    /**
     * Returns the given {@link SocketAddress} as a {@code byte[]}.
     */
    public static byte[] toBytes(SocketAddress address) {
        return toBytes(getAddress(address), getPort(address));
    }
    
    /**
     * Returns the given {@link InetAddress} and port as a {@code byte[]}.
     */
    public static byte[] toBytes(InetAddress address, int port) {
        byte[] addr = address.getAddress();
        byte[] dst = new byte[addr.length + 2];
        
        System.arraycopy(addr, 0, dst, 0, addr.length);
        dst[dst.length-2] = (byte)((port >> 8) & 0xFF);
        dst[dst.length-1] = (byte)((port     ) & 0xFF);
        
        return dst;
    }
    
    /**
     * Creates and returns an {@link InetAddress} from the given {@code byte[]}.
     * 
     * @see InetAddress#getByAddress(byte[])
     */
    public static InetAddress getByAddress(byte[] addr) {
        try {
            return InetAddress.getByAddress(addr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("UnknownHostException", e);
        }
    }
    
    /**
     * Creates and returns an {@link InetAddress} from the given {@code byte[]}
     * and hostname.
     * 
     * @see InetAddress#getByAddress(String, byte[])
     */
    public static InetAddress getByAddress(String host, byte[] addr) {
        try {
            return InetAddress.getByAddress(host, addr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("UnknownHostException", e);
        }
    }
}