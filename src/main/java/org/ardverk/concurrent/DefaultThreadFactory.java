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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.ardverk.lang.NullArgumentException;

/**
 * A very simple implementation of {@link ThreadFactory}.
 */
public class DefaultThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger();
    
    private final String name;
    
    private final boolean daemon;
    
    public DefaultThreadFactory(String name) {
        this(name, false);
    }
    
    public DefaultThreadFactory(String name, boolean daemon) {
        if (name == null) {
            throw new NullArgumentException("name");
        }
        
        this.name = name;
        this.daemon = daemon;
    }
    
    private String createName() {
        return name + "-" + counter.incrementAndGet();
    }
    
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, createName());
        thread.setDaemon(daemon);
        return thread;
    }
}