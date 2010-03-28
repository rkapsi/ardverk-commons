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
