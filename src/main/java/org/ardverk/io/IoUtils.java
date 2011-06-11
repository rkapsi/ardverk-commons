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

package org.ardverk.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import org.ardverk.collection.Iterables;
import org.ardverk.lang.ExceptionUtils;

/**
 * An utility class for I/O operations.
 */
public class IoUtils {
    
    private IoUtils() {}
    
    /**
     * Closes the given {@code Object}. Returns {@code true} on success
     * and {@code false} on failure. All {@link IOException}s will be
     * caught and no error will be thrown if the Object isn't any of
     * the supported types.
     * 
     * @see Closeable
     * @see #close(Closeable)
     * 
     * @see Socket
     * @see #close(Socket)
     * 
     * @see ServerSocket
     * @see #close(ServerSocket)
     * 
     * @see DatagramSocket
     * @see #close(DatagramSocket)
     * 
     * @see AtomicReference
     * @see #close(AtomicReference)
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
        } else if (o instanceof AtomicReference<?>) {
            return close(((AtomicReference<?>)o));
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
     * Closes the given {@link AtomicReference}
     */
    public static boolean close(AtomicReference<?> closeable) {
        if (closeable != null) {
            return close(closeable.get());
        }
        return false;
    }
    
    /**
     * Closes the given array of {@link Object}s.
     * 
     * @see #close(Object)
     */
    public static <T> boolean closeAll(T... closeables) {
        return closeAll(closeables, 0, closeables != null ? closeables.length : 0);
    }
    
    /**
     * Closes the given array of {@link Object}s.
     * 
     * @see #close(Object)
     */
    public static <T> boolean closeAll(T[] closeables, int offset, int length) {
        if (closeables != null) {
            return closeAll(Iterables.iterable(closeables, offset, length));
        }
        return false;
    }
    
    /**
     * Closes the given {@link Iterable} of {@link Object}s.
     */
    public static boolean closeAll(Iterable<?> closeables) {
        boolean success = true;
        if (closeables != null) {
            for (Object closeable : closeables) {
                success &= close(closeable);
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
    
    /**
     * Binds the {@link Bindable} to the given value.
     */
    public static <T> boolean bind(Bindable<? super T> bindable, T value) {
        if (bindable != null) {
            try {
                bindable.bind(value);
                return true;
            } catch (Exception err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }
    
    /**
     * Unbinds the given {@link Bindable}.
     */
    public static boolean unbind(Bindable<?> bindable) {
        if (bindable != null) {
            try {
                bindable.unbind();
                return true;
            } catch (Exception err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }
    
    /**
     * Unbinds the given array of {@link Bindable}s.
     */
    public static boolean unbindAll(Bindable<?>... bindables) {
        boolean success = true;
        if (bindables != null) {
            for (Bindable<?> c : bindables) {
                success &= unbind(c);
            }
        }
        return success;
    }
    
    /**
     * Unbinds the given {@link Iterable} of {@link Bindable}s
     */
    public static boolean unbindAll(Iterable<? extends Bindable<?>> bindables) {
        boolean success = true;
        if (bindables != null) {
            for (Bindable<?> c : bindables) {
                success &= unbind(c);
            }
        }
        return success;
    }
    
    public static boolean shutdownInput(Socket socket) {
        if (socket != null) {
            try {
                socket.shutdownInput();
            } catch (IOException err) {
                ExceptionUtils.exceptionCaught(err);
            }
            return true;
        }
        return false;
    }
    
    public static boolean shutdownOutput(Socket socket) {
        if (socket != null) {
            try {
                socket.shutdownOutput();
            } catch (IOException err) {
                ExceptionUtils.exceptionCaught(err);
            }
            return true;
        }
        return false;
    }
}