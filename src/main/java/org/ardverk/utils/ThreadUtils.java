package org.ardverk.utils;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

/**
 * An utility class for {@link Thread}s.
 */
public class ThreadUtils {

    private ThreadUtils() {}
    
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
