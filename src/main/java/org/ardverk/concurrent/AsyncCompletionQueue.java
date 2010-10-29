/*
 * Copyright 2009 Roger Kapsi
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

import java.util.concurrent.CompletionService;
import java.util.concurrent.TimeUnit;

/**
 * @see CompletionService
 */
public interface AsyncCompletionQueue<V> extends AsyncFutureListenerService<V> {
    
    public boolean offer(AsyncFuture<V> future);
    
    public AsyncFuture<V> take() throws InterruptedException;

    public AsyncFuture<V> poll();

    public AsyncFuture<V> poll(long timeout, TimeUnit unit) throws InterruptedException;
}
