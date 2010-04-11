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

import org.ardverk.utils.ExceptionUtils;

/**
 * A {@link ManagedRunnable} catches all {@link Exception}s and delegates
 * them to {@link #exceptionCaught(Throwable)}.
 * 
 * @see Runnable
 */
public abstract class ManagedRunnable implements Runnable {

    @Override
    public final void run() {
        try {
            doRun();
        } catch (Exception err) {
            exceptionCaught(err);
        }
    }
    
    /**
     * See {@link Runnable#run()}
     */
    protected abstract void doRun() throws Exception;
    
    /**
     * 
     */
    protected void exceptionCaught(Throwable t) {
        ExceptionUtils.exceptionCaught(t);
    }
}
