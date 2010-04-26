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

package org.ardverk.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NetworkUtils {

    private NetworkUtils() {}
    
    /**
     * Returns true if both {@link SocketAddress}es have the same {@link InetAddress}
     */
    public static boolean isSameAddress(SocketAddress a1, SocketAddress a2) {
        if (a1 == null) {
            throw new NullPointerException("address1");
        }
        
        if (a2 == null) {
            throw new NullPointerException("address2");
        }
        
        return ((InetSocketAddress)a1).getAddress().equals(
                ((InetSocketAddress)a2).getAddress());
    }
    
    /**
     * Returns true if the given port is valid. A valid (usable) port number
     * must be greater than zero and less than 0xFFFF.
     */
    public static boolean isValidPort(int port) {
        return 0 < port && port < 0xFFFF;
    }
    
    /**
     * Returns true if the given {@link SocketAddress} has a valid port 
     * number. A valid (usable) port number must be greater than zero and 
     * less than 0xFFFF.
     */
    public static boolean isValidPort(SocketAddress address) {
        return isValidPort(((InetSocketAddress)address).getPort());
    }
}
