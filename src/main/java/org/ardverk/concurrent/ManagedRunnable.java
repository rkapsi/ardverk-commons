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

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 
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
     * 
     */
    protected abstract void doRun() throws Exception;
    
    /**
     * 
     */
    protected void exceptionCaught(Throwable t) {
        Thread currentThread = Thread.currentThread();
        UncaughtExceptionHandler ueh 
            = currentThread.getUncaughtExceptionHandler();
        if (ueh == null) {
            ueh = Thread.getDefaultUncaughtExceptionHandler();
        }
        
        if (ueh != null) {
            ueh.uncaughtException(currentThread, t);
        }
    }
}
