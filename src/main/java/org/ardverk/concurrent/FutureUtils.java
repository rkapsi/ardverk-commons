package org.ardverk.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class FutureUtils {

    private FutureUtils() {}
    
    /**
     * Cancels the given {@link Future}.
     */
    public static boolean cancel(Future<?> future, 
            boolean mayInterruptIfRunning) {
        if (future != null) {
            return future.cancel(mayInterruptIfRunning);
        }
        return false;
    }
    
    /**
     * Cancels the given {@link AtomicReference} of an {@link Future}.
     */
    public static boolean cancel(AtomicReference<? extends Future<?>> futureRef, 
            boolean mayInterruptIfRunning) {
        if (futureRef != null) {
            return cancel(futureRef.get(), mayInterruptIfRunning);
        }
        return false;
    }
}
