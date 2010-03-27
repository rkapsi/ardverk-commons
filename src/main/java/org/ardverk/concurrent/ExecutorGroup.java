package org.ardverk.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 
 */
public class ExecutorGroup implements Executor {

    public static final Scheduler ONE = new DefaultScheduler(1);
    
    public static final Scheduler DRAIN = new DefaultScheduler(-1);
    
    private final Executor executor;
    
    private final Scheduler scheduler;
    
    private final Queue<Runnable> queue;
    
    private final Runnable processor = new Runnable() {
        @Override
        public void run() {
            try {
                process();
            } finally {
                reschedule();
            }
        }
    };
    
    private boolean scheduled = false;
    
    private boolean open = true;
    
    public ExecutorGroup(Executor executor) {
        this(executor, new LinkedList<Runnable>());
    }
    
    public ExecutorGroup(Executor executor, Queue<Runnable> queue) {
        this(executor, ONE, queue);
    }
    
    public ExecutorGroup(Executor executor, Scheduler scheduler) {
        this(executor, scheduler, new LinkedList<Runnable>());
    }
    
    public ExecutorGroup(Executor executor, Scheduler scheduler, 
            Queue<Runnable> queue) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        
        if (scheduler == null) {
            throw new NullPointerException("scheduler");
        }
        
        if (queue == null) {
            throw new NullPointerException("queue");
        }
        
        this.executor = executor;
        this.scheduler = scheduler;
        this.queue = queue;
    }
    
    public Executor getExecutor() {
        return executor;
    }
    
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    public Queue<Runnable> getQueue() {
        return queue;
    }
    
    public synchronized void shutdown() {
        if (open) {
            open = false;
            
            if (queue.isEmpty()) {
                List<Runnable> tasks = Collections.emptyList();
                executorClosed(tasks);
            }
        }
    }
    
    public synchronized List<Runnable> shutdownNow() {
        if (open) {
            open = false;
            
            List<Runnable> copy 
                = new ArrayList<Runnable>(queue);
            queue.clear();
            
            executorClosed(copy);
            
            return copy;
        }
        return Collections.emptyList();
    }
    
    public synchronized boolean isShutdown() {
        return !open;
    }
    
    public synchronized boolean isTerminated() {
        return !open && queue.isEmpty();
    }
    
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public synchronized int size() {
        return queue.size();
    }
    
    public synchronized boolean awaitTermination(long timeout, TimeUnit unit) 
            throws InterruptedException {
        
        if (timeout < 0L) {
            throw new IllegalArgumentException("timeout=" + timeout);
        }
        
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        
        if (!isTerminated()) {
            if (timeout == 0L) {
                wait();
            } else {
                unit.timedWait(this, timeout);
            }
        }
        
        return isTerminated();
    }
    
    @Override
    public void execute(Runnable task) throws RejectedExecutionException {
        boolean success = offer(task);
        if (!success) {
            throw new RejectedExecutionException();
        }
    }
    
    private synchronized boolean offer(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        
        boolean added = false;
        if (open) {
            added = queue.offer(task);
            if (added) {
                executeIfNotAlready();
            }
        }
        return added;
    }
    
    private synchronized Runnable poll() {
        return queue.poll();
    }
    
    private synchronized boolean executeIfNotAlready() {
        if (!scheduled && !queue.isEmpty()) {
            scheduled = true;
            executor.execute(processor);
            return true;
        }
        return false;
    }
    
    private synchronized boolean reschedule() {
        scheduled = !queue.isEmpty();
        if (scheduled) {
            executor.execute(processor);
        }
        
        if (isTerminated()) {
            notifyAll();
        }
        
        return scheduled;
    }
    
    private void process() {
        Runnable task = null;
        long timeStamp = System.currentTimeMillis();
        int index = 0;
        
        try {
            while (doNext(index, timeStamp) && (task = poll()) != null) {
                doRun(task, index, timeStamp);
                ++index;
            }
        } finally {
            complete(index, timeStamp);
        }
    }
    
    protected boolean doNext(int index, long timeStamp) {
        return scheduler.doNext(this, index, timeStamp);
    }
    
    protected void doRun(Runnable task, int index, long timeStamp) {
        try {
            task.run();
        } catch (Exception t) {
            exceptionCaught(Thread.currentThread(), task, t);
        }
    }
    
    protected void exceptionCaught(Thread thread, Runnable task, Throwable t) {
        UncaughtExceptionHandler ueh 
            = thread.getUncaughtExceptionHandler();
        if (ueh == null) {
            ueh = Thread.getDefaultUncaughtExceptionHandler();
        }
        
        if (ueh != null) {
            ueh.uncaughtException(thread, t);
        }
    }
    
    protected void complete(int count, long timeStamp) {
    }
    
    protected void executorClosed(List<? extends Runnable> tasks) {
    }
}
