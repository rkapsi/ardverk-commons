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

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionUtils {

    private static final UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER 
        = new DefaultUncaughtExceptionHandler();
    
    private ExceptionUtils() {}
    
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
