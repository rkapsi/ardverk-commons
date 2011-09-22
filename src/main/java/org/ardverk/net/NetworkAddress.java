package org.ardverk.net;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * A {@link NetworkAddress} is an unresolved Host:Port pair.
 */
public class NetworkAddress implements Serializable, Cloneable {
    
    private static final long serialVersionUID = 7480073476378072848L;
    
    public static NetworkAddress valueOf(String host, int port) {
        if (host == null) {
            throw new NullPointerException("host");
        }
        
        host = host.trim();
        if (host.isEmpty()) {
            throw new IllegalArgumentException("host=" + host);
        }
        
        if (!NetworkUtils.isValidPort(port)) {
            throw new IllegalArgumentException("port=" + port);
        }
        
        return new NetworkAddress(InetSocketAddress.createUnresolved(host, port));
    }
    
    private final InetSocketAddress address;
    
    private NetworkAddress(InetSocketAddress address) {
        this.address = address;
    }
    
    /**
     * Returns the host string.
     */
    public String getHost() {
        return address.getHostName();
    }
    
    /**
     * Returns the port number.
     */
    public int getPort() {
        return address.getPort();
    }
    
    /**
     * Same as {@code asSocketAddress(true)}
     * 
     * @see #asSocketAddress(boolean)
     */
    public InetSocketAddress asInetSocketAddress() {
        return asInetSocketAddress(true);
    }
    
    /**
     * Creates and returns a resolved or unresolved {@link InetSocketAddress}.
     * 
     * @see InetSocketAddress
     * @see InetSocketAddress#createUnresolved(String, int)
     */
    public InetSocketAddress asInetSocketAddress(boolean unresolved) {
        if (unresolved) {
            return address;
        }
        return NetworkUtils.getResolved(address);
    }
    
    @Override
    public int hashCode() {
        return address.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NetworkAddress)) {
            return false;
        }
        
        NetworkAddress other = (NetworkAddress)o;
        return address.equals(other.address);
    }
    
    @Override
    public NetworkAddress clone() {
        return this;
    }
    
    @Override
    public String toString() {
        return address.toString();
    }
    
    public static NetworkAddress[] parse(String addresses) {
        return parse0(addresses, -1);
    }
    
    public static NetworkAddress[] parse(String addresses, int defaultPort) {
        if (!NetworkUtils.isValidPort(defaultPort)) {
            throw new IllegalArgumentException("defaultPort=" + defaultPort);
        }
        
        return parse0(addresses, defaultPort);
    }
    
    private static NetworkAddress[] parse0(String addresses, int defaultPort) {
        List<NetworkAddress> dst = new ArrayList<NetworkAddress>();
        
        for (String address : addresses.split(",")) {
            
            int p = address.lastIndexOf(':');
            if (p != -1) {
                String host = address.substring(0, p);
                int port = Integer.parseInt(address.substring(++p).trim());
                dst.add(NetworkAddress.valueOf(host, port));
            } else {
                
                if (defaultPort == -1) {
                    throw new IllegalArgumentException(
                            "Port number missing: " + address);
                }
                
                dst.add(NetworkAddress.valueOf(address, defaultPort));
            }
        }
        
        return dst.toArray(new NetworkAddress[0]);
    }
}
