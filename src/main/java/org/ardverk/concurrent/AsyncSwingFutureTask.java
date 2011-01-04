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

import java.util.concurrent.Callable;

import javax.swing.SwingUtilities;

/**
 * An {@link AsyncFutureTask} that fires its events on the Swing 
 * Event Dispatcher Thread (EDT).
 * 
 * See also {@link AsyncSwingFutureListener} for an alternative implementation.
 * 
 * @see SwingUtilities#isEventDispatchThread()
 * @see SwingUtilities#invokeLater(Runnable)
 */
public class AsyncSwingFutureTask<V> extends AsyncFutureTask<V> {

    public AsyncSwingFutureTask() {
        super();
    }

    public AsyncSwingFutureTask(Callable<V> callable) {
        super(callable);
    }

    public AsyncSwingFutureTask(Runnable task, V value) {
        super(task, value);
    }

    /**
     * @see SwingUtilities#isEventDispatchThread()
     */
    @Override
    protected boolean isEventThread() {
        return SwingUtilities.isEventDispatchThread();
    }
    
    /**
     * @see SwingUtilities#invokeLater(Runnable)
     */
    @Override
    protected void fireOperationComplete(final AsyncFutureListener<V> first,
            final AsyncFutureListener<V>... others) {
        
        if (first == null) {
            return;
        }
        
        Runnable event = new Runnable() {
            @Override
            public void run() {
                AsyncSwingFutureTask.super.fireOperationComplete(first, others);
            }
        };
        
        SwingUtilities.invokeLater(event);
    }
}