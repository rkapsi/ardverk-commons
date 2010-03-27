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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ExecutorGroup} is an {@link Executor} that preserves the 
 * execution order in which {@link Runnable}s were added if the underlying 
 * {@link Executor} is using more than one {@link Thread}.
 * 
 * In other words it's like an {@link Executors#newSingleThreadExecutor()}
 * but done with more than one {@link Thread}.
 * 
 * For example there could be a Connection class that has the ability to
 * send messages. The requirement is to send messages in sequential order
 * but off-load the operation to a different {@link Thread} and at the same
 * time utilize more one {@link Thread}.
 * 
 * static Executor EXECUTOR = Executors.newFixedThreadPool(16);
 * 
 * class Connection {
 *     
 *     private final ExecutorGroup group = new ExecutorGroup(EXECUTOR);
 *     
 *     public void sendMessage() {
 *         group.execute(new Runnable() {
 *             public void run() {
 *                 // Do Something
 *             }
 *         });
 *     }
 * }
 */
public class ExecutorGroup implements Executor {

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
    
    private boolean closed = false;
    
    public ExecutorGroup(Executor executor) {
        this(executor, new LinkedList<Runnable>());
    }
    
    public ExecutorGroup(Executor executor, Queue<Runnable> queue) {
        this(executor, DEFAULT, queue);
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
    
    /**
     * Returns the {@link Executor}
     */
    public Executor getExecutor() {
        return executor;
    }
    
    /**
     * Returns the {@link Scheduler}
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    /**
     * Returns the {@link Queue}
     */
    public Queue<Runnable> getQueue() {
        return queue;
    }
    
    /**
     * Initiates an orderly shutdown in which previously submitted tasks 
     * are executed, but no new tasks will be accepted.
     */
    public synchronized void shutdown() {
        if (open) {
            open = false;
            
            if (!closed && !scheduled) {
                closed = true;
                
                List<Runnable> tasks 
                    = Collections.emptyList();
                executorClosed(tasks);
            }
        }
    }
    
    /**
     * Attempts to stop all actively executing tasks, halts the processing 
     * of waiting tasks, and returns a list of the tasks that were awaiting 
     * execution.
     */
    public synchronized List<Runnable> shutdownNow() {
        if (open) {
            open = false;
            
            List<Runnable> copy 
                = new ArrayList<Runnable>(queue);
            queue.clear();
            
            if (!closed && !scheduled) {
                closed = true;
                executorClosed(copy);
            }
            
            return copy;
        }
        return Collections.emptyList();
    }
    
    /**
     * Returns true if this executor has been shut down.
     */
    public synchronized boolean isShutdown() {
        return !open;
    }
    
    /**
     * Returns true if all tasks have completed following shut down.
     */
    public synchronized boolean isTerminated() {
        return !open && queue.isEmpty();
    }
    
    /**
     * Returns true if the {@link Queue} is empty.
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * Returns the {@link Queue} size.
     */
    public synchronized int size() {
        return queue.size();
    }
    
    /**
     * Blocks until all tasks have completed execution after a shutdown 
     * request, or the timeout occurs, or the current thread is interrupted, 
     * whichever happens first.
     */
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
        if (!scheduled) {
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
            if (!closed) {
                closed = true;
                List<Runnable> tasks 
                    = Collections.emptyList();
                executorClosed(tasks);
            }
            
            notifyAll();
        }
        
        return scheduled;
    }
    
    private void process() {
        Runnable task = null;
        long timeStamp = System.currentTimeMillis();
        int index = 0;
        boolean doContinue = true;
        
        try {
            while (doContinue && doNext(index, timeStamp) 
                    && (task = poll()) != null) {
                doContinue = doRun(task, index, timeStamp);
                ++index;
            }
        } finally {
            complete(index, timeStamp);
        }
    }
    
    protected boolean doNext(int index, long timeStamp) {
        return scheduler.doNext(this, index, timeStamp);
    }
    
    protected boolean doRun(Runnable task, int index, long timeStamp) {
        try {
            task.run();
        } catch (Exception t) {
            exceptionCaught(Thread.currentThread(), task, t);
        }
        return true;
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
