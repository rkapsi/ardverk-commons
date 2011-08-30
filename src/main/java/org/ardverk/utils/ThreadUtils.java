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

package org.ardverk.utils;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * An utility class for {@link Thread}s.
 */
public class ThreadUtils {

    private ThreadUtils() {}
    
    /**
     * 
     */
    public static ThreadInfo[] getStackTraces() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.dumpAllThreads(true, true);
    }
    
    /**
     * 
     */
    public static long[] getDeadlocks() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.findDeadlockedThreads();
    }
    
    /**
     * Turns the given {@link ThreadInfo} into a {@link String}.
     */
    public static String toString(ThreadInfo[] threads) {
        StringBuilder buffer = new StringBuilder();
        
        for (ThreadInfo info : threads) {
            buffer.append("\"").append(info.getThreadName())
                .append("\" (id=").append(info.getThreadId()).append(")");
            
            buffer.append(" ").append(info.getThreadState())
                .append(" on ").append(info.getLockName()).append(" owned by ");
            
            buffer.append("\"").append(info.getLockOwnerName())
                .append("\" (id=").append(info.getLockOwnerId()).append(")");
            
            if (info.isSuspended()) {
                buffer.append(" (suspended)");
            }
            
            if (info.isInNative()) {
                buffer.append(" (is native)");
            }
            buffer.append("\n");
            
            StackTraceElement[] trace = info.getStackTrace();
            for (int i = 0; i < trace.length; i++) {
                buffer.append("\tat ").append(trace[i]).append("\n");
                
                if (i == 0) {
                    buffer.append(getLockState(info));
                }
                
                buffer.append(getLockedMonitors(info, i));
            }
            
            buffer.append("\n");
        }
        
        return buffer.toString();
    }
    
    /**
     * Returns the names of the locks this {@link Thread} 
     * is blocking or waiting on.
     * 
     * @see ThreadInfo#getLockInfo()
     * @see ThreadInfo#getThreadState()
     */
    private static String getLockState(ThreadInfo info) {
        StringBuilder buffer = new StringBuilder();
        
        LockInfo lockInfo = info.getLockInfo();
        Thread.State state = info.getThreadState();
        
        switch (state) {
            case BLOCKED: 
                buffer.append("\t-  blocked on ").append(lockInfo).append("\n");
                break;
            case WAITING:
            case TIMED_WAITING:
                buffer.append("\t-  waiting on ").append(lockInfo).append("\n");
                break;
            default:
                break;
        }
        
        return buffer.toString();
    }
    
    /**
     * Returns the names of the locks this {@link Thread} has locked.
     * 
     * @see ThreadInfo#getLockedMonitors()
     */
    private static String getLockedMonitors(ThreadInfo info, int stackIndex) {
        StringBuilder buffer = new StringBuilder();
        
        MonitorInfo[] monitors = info.getLockedMonitors();
        for (MonitorInfo monitor : monitors) {
            int depth = monitor.getLockedStackDepth();
            if (depth == stackIndex) {
                buffer.append("\t-  locked ").append(monitor).append("\n");
            }
        }
        return buffer.toString();
    }
}