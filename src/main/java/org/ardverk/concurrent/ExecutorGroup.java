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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.ardverk.lang.Arguments;
import org.ardverk.lang.NullArgumentException;
import org.ardverk.utils.ExceptionUtils;

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
 * time utilize more than one {@link Thread}.
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
public class ExecutorGroup extends AbstractExecutorQueue<Runnable> implements Executor {
    
    private final Scheduler scheduler;
    
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
        this(executor, DefaultScheduler.DEFAULT, queue);
    }
    
    public ExecutorGroup(Executor executor, Scheduler scheduler) {
        this(executor, scheduler, new LinkedList<Runnable>());
    }
    
    public ExecutorGroup(Executor executor, Scheduler scheduler, 
            Queue<Runnable> queue) {
        super(executor, queue);
        this.scheduler = Arguments.notNull(scheduler, "scheduler");
    }
    
    /**
     * Returns the {@link Scheduler}
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    @Override
    public synchronized void shutdown() {
        if (open) {
            open = false;
            
            if (!closed && !scheduled) {
                closed = true;
                
                List<Runnable> tasks 
                    = Collections.emptyList();
                terminated(tasks);
            }
        }
    }
    
    @Override
    public synchronized List<Runnable> shutdownNow() {
        if (open) {
            open = false;
            
            List<Runnable> copy 
                = new ArrayList<Runnable>(queue);
            queue.clear();
            
            if (!closed && !scheduled) {
                closed = true;
                terminated(copy);
            }
            
            return copy;
        }
        return Collections.emptyList();
    }
    
    @Override
    public synchronized boolean isShutdown() {
        return !open;
    }
    
    @Override
    public synchronized boolean isTerminated() {
        return !open && queue.isEmpty();
    }
    
    @Override
    public synchronized boolean awaitTermination(long timeout, TimeUnit unit) 
            throws InterruptedException {
        
        if (timeout < 0L) {
            throw new IllegalArgumentException("timeout=" + timeout);
        }
        
        if (unit == null) {
            throw new NullArgumentException("unit");
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
    
    /**
     * 
     */
    private synchronized boolean offer(Runnable task) {
        if (task == null) {
            throw new NullArgumentException("task");
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
    
    /**
     * 
     */
    private synchronized Runnable poll() {
        return queue.poll();
    }
    
    /**
     * 
     */
    private synchronized boolean executeIfNotAlready() {
        if (!scheduled) {
            scheduled = true;
            executor.execute(processor);
            return true;
        }
        return false;
    }
    
    /**
     * 
     */
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
                terminated(tasks);
            }
            
            notifyAll();
        }
        
        return scheduled;
    }
    
    /**
     * 
     */
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
    
    /**
     * 
     */
    protected boolean doNext(int index, long timeStamp) {
        return scheduler.doNext(this, index, timeStamp);
    }
    
    /**
     * 
     */
    protected boolean doRun(Runnable task, int index, long timeStamp) {
        try {
            task.run();
        } catch (Exception t) {
            exceptionCaught(Thread.currentThread(), task, t);
        }
        return true;
    }
    
    /**
     * 
     */
    protected void exceptionCaught(Thread thread, Runnable task, Throwable t) {
        ExceptionUtils.exceptionCaught(t);
    }
    
    /**
     * 
     */
    protected void complete(int count, long timeStamp) {
    }
    
    /**
     * Method invoked when the {@link ExecutorGroup} has terminated. 
     * Default implementation does nothing.
     */
    protected void terminated(List<? extends Runnable> tasks) {
    }
}
