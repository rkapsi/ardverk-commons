package org.ardverk.concurrent;

/**
 * 
 */
public interface Scheduler {
   
    /**
     * 
     */
    public boolean doNext(ExecutorGroup group, int index, long timeStamp);
}