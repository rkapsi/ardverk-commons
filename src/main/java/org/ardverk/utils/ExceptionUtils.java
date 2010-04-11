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

package org.ardverk.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionUtils {

    private static final UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER 
        = new DefaultUncaughtExceptionHandler();
    
    private ExceptionUtils() {}
    
    /**
     * An utility method to wrap {@link Throwable}s in {@link IOException}s
     * unless they are already an {@link IOException} in which case it will
     * simply cast and return it.
     */
    public static IOException toIoException(Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        
        if (cause instanceof IOException) {
            return (IOException)cause;
        }
        
        return new IOException(cause);
    }
    
    /**
     * Returns true if the given {@link Throwable} is or rather was caused
     * by an instance of the given {@link Class}.
     */
    public static boolean isCausedBy(Throwable t, Class<? extends Throwable> clazz) {
        return getCause(t, clazz) != null;
    }
    
    /**
     * Returns the first {@link Exception} from the cause chain that is an
     * instance of the given {@link Class}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T getCause(Throwable t, Class<T> clazz) {
        while(t != null) {
            if (clazz.isInstance(t)) {
                return (T)t;
            }
            t = t.getCause();
        }
        return null;
    }
    
    /**
     * An utility method that will pass the given {@link Throwable}
     * to the calling {@link Thread}'s {@link UncaughtExceptionHandler}.
     */
    public static void exceptionCaught(Throwable t) {
        Thread thread = Thread.currentThread();
        UncaughtExceptionHandler ueh 
            = thread.getUncaughtExceptionHandler();
        if (ueh == null) {
            ueh = Thread.getDefaultUncaughtExceptionHandler();
            
            if (ueh == null) {
                ueh = UNCAUGHT_EXCEPTION_HANDLER;
            }
        }
        
        ueh.uncaughtException(thread, t);
    }

    public static String toString(Throwable t) {
        if (t == null) {
            return null;
        }
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        t.printStackTrace(pw);
        pw.close();
        return out.toString();
    }
    
    /**
     * An {@link UncaughtExceptionHandler} that is printing the
     * stack trace.
     */
    private static class DefaultUncaughtExceptionHandler 
            implements UncaughtExceptionHandler {
        
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    }
}
