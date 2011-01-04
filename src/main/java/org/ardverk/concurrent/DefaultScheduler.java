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

import java.util.Queue;

/**
 * A default implementation of a {@link Scheduler}.
 */
public class DefaultScheduler implements Scheduler {
    
    /**
     * Takes one {@link Runnable} from the {@link Queue}, executes
     * it and reschedules itself if necessary.
     */
    public static final Scheduler DEFAULT = new DefaultScheduler(1);
    
    /**
     * Takes all {@link Runnable}s from the queue, executes them
     * and reschedules itself if necessary.
     */
    public static final Scheduler DRAIN = new DefaultScheduler(-1);
    
    private final int max;
    
    public DefaultScheduler(int max) {
        if (max < 0 && max != -1) {
            throw new IllegalArgumentException("max=" + max);
        }
        
        this.max = max;
    }

    @Override
    public boolean doNext(ExecutorGroup group, int index, long timeStamp) {
        return index < max || max == -1;
    }
}