/*
 * Copyright 2010-2011 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.utils;

import java.lang.instrument.Instrumentation;
import java.lang.management.ThreadInfo;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.DefaultThreadFactory;
import org.ardverk.concurrent.ManagedRunnable;

/**
 * An utility class for Deadlock scanning.
 */
public class DeadlockScanner {
  
  /**
   * The default frequency to scan for deadlocks.
   */
  private static final long FREQUENCY 
    = PropertiesUtils.getLong(DeadlockScanner.class, 
        "frequency", 3L * 1000L);
  
  /**
   * The default {@link Callback} which prints the deadlock information
   * to {@link System#err}.
   */
  private static final Callback CALLBACK = new Callback() {
    @Override
    public void deadlock(Deadlock deadlock) {
      System.err.println(deadlock);
    }
  };
  
  /**
   * The deadlock scanner {@link Thread}.
   */
  private static final ScheduledExecutorService EXECUTOR 
    = Executors.newSingleThreadScheduledExecutor(
      new DefaultThreadFactory("DeadlockScannerThread", true));
  
  /**
   * The deadlock scanner {@link ScheduledFuture}.
   */
  private static ScheduledFuture<?> FUTURE = null;
  
  private DeadlockScanner() {}
  
  /**
   * Starts the {@link DeadlockScanner}.
   */
  public static synchronized void start() {
    start(CALLBACK, FREQUENCY, TimeUnit.MILLISECONDS);
  }
  
  /**
   * Starts the {@link DeadlockScanner}.
   */
  public static synchronized void start(Callback callback) {
    start(callback, FREQUENCY, TimeUnit.MILLISECONDS);
  }
  
  /**
   * Starts the {@link DeadlockScanner}.
   */
  public static synchronized void start(long frequency, TimeUnit unit) {
    start(CALLBACK, frequency, unit);
  }
  
  /**
   * Starts the {@link DeadlockScanner}.
   */
  public static synchronized void start(final Callback callback, 
      long frequency, TimeUnit unit) {
    
    if (callback == null) {
      throw new NullPointerException("callback");
    }
    
    if (frequency < 0L) {
      throw new IllegalArgumentException("frequency=" + frequency);
    }
    
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    
    if (FUTURE != null) {
      FUTURE.cancel(true);
    }
    
    Runnable task = new ManagedRunnable() {
      @Override
      protected void doRun() {
        checkForDeadlock(callback);
      }
    };
    
    FUTURE = EXECUTOR.scheduleWithFixedDelay(
        task, frequency, frequency, unit);
  }
  
  /**
   * Stops the {@link DeadlockScanner}.
   */
  public static synchronized void stop() {
    if (FUTURE != null) {
      FUTURE.cancel(true);
    }
  }
  
  /**
   * Returns true if the {@link DeadlockScanner} is running.
   */
  public synchronized boolean isRunning() {
    return FUTURE != null && !FUTURE.isDone();
  }
  
  /**
   * The methods which scans for deadlocks.
   */
  private static void checkForDeadlock(Callback callback) {
    
    long[] deadlocks = ThreadUtils.getDeadlocks();
    
    // Any deadlocks found?
    if (deadlocks == null || deadlocks.length == 0) {
      return;
    }
    
    ThreadInfo[] threads = ThreadUtils.getThreadInfo(deadlocks);
    
    callback.deadlock(new Deadlock(deadlocks, threads));
  }

  /**
   * Java {@link Instrumentation}'s pre-main method
   * 
   * @see java.lang.instrument
   */
  public static void premain(String args) {
    start();
  }
  
  /**
   * Java {@link Instrumentation}'s agent-main method
   * 
   * @see java.lang.instrument
   */
  public static void agentmain(String args) {
    start();
  }
  
  /**
   * A callback interface that is being called by the 
   * {@link DeadlockScanner} if a deadlock occurred.
   */
  public static interface Callback {
    
    /**
     * Called if a deadlock occurred.
     * 
     * @param deadlock The details of the deadlock
     */
    public void deadlock(Deadlock deadlock);
  }
  
  /**
   * An object that holds all details of a deadlock.
   */
  public static class Deadlock {
    
    private final long[] deadlocks;
    
    private final ThreadInfo[] threads;
    
    private Deadlock(long[] deadlocks, ThreadInfo[] threads) {
      this.deadlocks = deadlocks;
      this.threads = threads;
    }
    
    /**
     * Returns the IDs of all {@link Thread}s that are in a deadlock
     * 
     * @see Thread#getId()
     */
    public long[] getDeadlocks() {
      return deadlocks;
    }
    
    /**
     * 
     */
    public ThreadInfo[] getThreads() {
      return threads;
    }
    
    @Override
    public String toString() {
      return ThreadUtils.toString(threads);
    }
  }
  
  /*public static void main(String[] args) {
    DeadlockScanner.start();
    
    final Object lock1 = new Object();
    final Object lock2 = new Object();
    
    Runnable task1 = new Runnable() {
      @Override
      public void run() {
        while(true) {
          synchronized (lock1) {
            synchronized (lock2) {
              System.out.println("A");
            }
          }
        }
      }
    };
    
    Runnable task2 = new Runnable() {
      @Override
      public void run() {
        while(true) {
          synchronized (lock2) {
            synchronized (lock1) {
              System.out.println("B");
            }
          }
        }
      }
    };
    
    new Thread(task1, "A").start();
    task2.run();
  }*/
}