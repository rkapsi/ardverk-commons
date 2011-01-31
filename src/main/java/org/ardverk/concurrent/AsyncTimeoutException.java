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

package org.ardverk.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The {@link AsyncTimeoutException} is thrown if a {@link AsyncFuture}
 * has timed out.
 * 
 * @see TimeoutException
 */
public class AsyncTimeoutException extends TimeoutException {

    private static final long serialVersionUID = -8502819618973540700L;
    
    private final long timeoutInMillis;
    
    public AsyncTimeoutException(long timeout, TimeUnit unit) {
        super(message(null, timeout, unit));
        this.timeoutInMillis = unit.toMillis(timeout);
    }

    public AsyncTimeoutException(String message, long timeout, TimeUnit unit) {
        super(message(message, timeout, unit));
        this.timeoutInMillis = unit.toMillis(timeout);
    }
    
    public long getTimeout(TimeUnit unit) {
        return unit.convert(timeoutInMillis, TimeUnit.MILLISECONDS);
    }
    
    public long getTimeoutInMillis() {
        return getTimeout(TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates and returns an exception message from the given arguments.
     */
    private static String message(String message, long timeout, TimeUnit unit) {
        if (message == null || message.isEmpty()) {
            return timeout + " " + unit;
        }
        
        return message + ": " + timeout + " " + unit;
    }
}
