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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 */
public interface ExecutorQueue<T extends Runnable> {

    /**
     * Returns true if the {@link ExecutorQueue} is shutdown.
     */
    public boolean isShutdown();
    
    /**
     * Returns true if the {@link ExecutorQueue} is terminated.
     */
    public boolean isTerminated();
    
    /**
     * Shuts down the {@link ExecutorQueue}. Queued tasks continue to 
     * be executed but no new tasks will be accepted.
     */
    public void shutdown();
    
    /**
     * Shuts down the {@link ExecutorQueue}, clears the queue and returns
     * all tasks that were in the queue.
     */
    public List<T> shutdownNow();
    
    /**
     * Waits for the {@link ExecutorQueue} to terminate. Returns true if 
     * the {@link ExecutorQueue} is terminated and false if it isn't.
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) 
            throws InterruptedException;
    
    /**
     * Executes the given task.
     */
    public void execute(T task);
}
