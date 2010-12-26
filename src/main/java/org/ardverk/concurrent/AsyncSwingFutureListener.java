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

import javax.swing.SwingUtilities;

/**
 * An abstract implementation of {@link AsyncFutureListener} that delegates
 * all {@link #operationComplete(AsyncFuture)} events to the Swing Event
 * Dispatcher Thread (EDT).
 * 
 * See also {@link AsyncSwingFutureTask} for an alternative implementation.
 * 
 * @see SwingUtilities#invokeLater(Runnable)
 */
public abstract class AsyncSwingFutureListener<V> implements AsyncFutureListener<V> {

    private final boolean ivokeLater;
    
    /**
     * Creates an {@link AsyncSwingFutureListener}.
     * 
     * @see SwingUtilities#invokeLater(Runnable)
     */
    public AsyncSwingFutureListener() {
        this(true);
    }
    
    /**
     * Creates an {@link AsyncSwingFutureListener}.
     */
    public AsyncSwingFutureListener(boolean invokeLater) {
        this.ivokeLater = invokeLater;
    }
    
    @Override
    public final void operationComplete(final AsyncFuture<V> future) {
        if (!ivokeLater && SwingUtilities.isEventDispatchThread()) {
            operationCompleteEDT(future);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    operationCompleteEDT(future);
                }
            });
        }
    }
    
    /**
     * Called by {@link #operationComplete(AsyncFuture)} on the Swing Event
     * Dispatcher Thread (EDT).
     * 
     * @see SwingUtilities#invokeLater(Runnable)
     */
    protected abstract void operationCompleteEDT(AsyncFuture<V> future);
}
