/*
 * Copyright 2009, 2010 Roger Kapsi
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

/**
 * An {@link AsyncProcessFuture} is an {@link AsyncFuture} that is
 * backed by an {@link AsyncProcess}.
 */
public interface AsyncProcessFuture<V> extends AsyncFuture<V> {
    
    /**
     * Returns the timeout for the {@link AsyncProcessFuture}
     */
    public long getTimeout(TimeUnit unit);
    
    /**
     * Returns the timeout for the {@link AsyncProcessFuture}
     * in milliseconds.
     */
    public long getTimeoutInMillis();
    
    /**
     * Returns true if the {@link AsyncProcessFuture} 
     * completed due to a timeout.
     */
    public boolean isTimeout();
}
