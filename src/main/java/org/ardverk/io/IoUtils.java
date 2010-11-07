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

package org.ardverk.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import org.ardverk.utils.ExceptionUtils;

/**
 * An utility class for I/O operations.
 */
public class IoUtils {
    
    private IoUtils() {}
    
    /**
     * Closes the given {@code Object} if it's a {@link Closeable},
     * {@link Socket}, {@link ServerSocket} or {@link DatagramSocket}.
     */
    public static boolean close(Object o) {
        if (o instanceof Closeable) {
            return close((Closeable)o);
        } else if (o instanceof Socket) {
            return close((Socket)o);
        } else if (o instanceof ServerSocket) {
            return close((ServerSocket)o);
        } else if (o instanceof DatagramSocket) {
            return close((DatagramSocket)o);
        }
        return false;
    }
    
    /**
     * Closes the given {@link Socket}
     */
    public static boolean close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
                return true;
            } catch (IOException err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }
    
    /**
     * Closes the given {@link ServerSocket}
     */
    public static boolean close(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
                return true;
            } catch (IOException err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }
    
    /**
     * Closes the given {@link DatagramSocket}
     */
    public static boolean close(DatagramSocket socket) {
        if (socket != null) {
            socket.close();
            return true;
        }
        return false;
    }
    
    /**
     * Closes the given {@link Closeable}
     */
    public static boolean close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }
    
    /**
     * Closes the given array of {@link Closeable}s
     */
    public static boolean closeAll(Closeable... closeables) {
        boolean success = true;
        if (closeables != null) {
            for (Closeable c : closeables) {
                success &= close(c);
            }
        }
        return success;
    }
    
    /**
     * Closes the given {@link Iterable} of {@link Closeable}s
     */
    public static boolean closeAll(Iterable<? extends Closeable> closeables) {
        boolean success = true;
        if (closeables != null) {
            for (Closeable c : closeables) {
                success &= close(c);
            }
        }
        return success;
    }
    
    /**
     * Flushes the given {@link Flushable}
     */
    public static boolean flush(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
                return true;
            } catch (IOException err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }
    
    /**
     * Flushes the given array of {@link Flushable}s.
     */
    public static boolean flushAll(Flushable... flushables) {
        boolean success = true;
        if (flushables != null) {
            for (Flushable c : flushables) {
                success &= flush(c);
            }
        }
        return success;
    }
    
    /**
     * Flushes the given {@link Iterable} of {@link Flushable}s
     */
    public static boolean flushAll(Iterable<? extends Flushable> flushables) {
        boolean success = true;
        if (flushables != null) {
            for (Flushable c : flushables) {
                success &= flush(c);
            }
        }
        return success;
    }
}
