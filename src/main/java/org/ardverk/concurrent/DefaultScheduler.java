package org.ardverk.concurrent;

public class DefaultScheduler implements Scheduler {
    
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