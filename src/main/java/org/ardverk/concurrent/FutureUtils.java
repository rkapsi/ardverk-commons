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

package org.ardverk.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Misc utilities for {@link Future}s.
 */
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
     * Cancels the given {@link AtomicReference} of a {@link Future}.
     */
    public static boolean cancel(AtomicReference<? extends Future<?>> futureRef, 
            boolean mayInterruptIfRunning) {
        if (futureRef != null) {
            return cancel(futureRef.get(), mayInterruptIfRunning);
        }
        return false;
    }
    
    /**
     * Cancels the given {@link Iterable} of {@link Future}s.
     */
    public static boolean cancelAll(Iterable<? extends Future<?>> futures, 
            boolean mayInterruptIfRunning) {
        boolean success = true;
        if (futures != null) {
            for (Future<?> future : futures) {
                success &= cancel(future, mayInterruptIfRunning);
            }
        }
        return success;
    }
}
