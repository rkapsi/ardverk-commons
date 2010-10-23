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

import java.util.Queue;
import java.util.concurrent.Executor;

import org.ardverk.lang.Arguments;

abstract class AbstractExecutorQueue<T extends Runnable> implements ExecutorQueue<T> {

    protected final Executor executor;
    
    protected final Queue<T> queue;
    
    public AbstractExecutorQueue(Executor executor, Queue<T> queue) {
        this.executor = Arguments.notNull(executor, "executor");
        this.queue = Arguments.notNull(queue, "queue");
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public Queue<T> getQueue() {
        return queue;
    }

    @Override
    public synchronized int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
