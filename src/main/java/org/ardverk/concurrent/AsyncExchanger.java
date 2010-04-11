/*
 * Copyright 2009 Roger Kapsi
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

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ardverk.lang.NullArgumentException;
import org.ardverk.utils.ExceptionUtils;

/**
 * The {@link AsyncExchanger} is an one-way synchronization point for 
 * {@link Thread}s. One or more {@link Thread}s can wait for the arrival 
 * of a value by calling the {@link #get()} method which will block and 
 * suspend the {@link Thread}s until an another Thread sets a return 
 * value or an {@link Exception} which will be thrown by the 
 * {@link #get()} method.
 */
public class AsyncExchanger<V, E extends Throwable> {
    
    /** Used during construction time as a substitute for 'this'  */
    private static final Object THIS = new Object();
    
    /** The lock that is being used for synchronization */
    private final Object lock;
    
    /** Flag for whether or not we're done */
    private boolean done = false;
    
    /** Flag for whether or not the exchanger was canceled */
    private boolean cancelled = false;
    
    /** The value we're going to return */
    private V value;
    
    /** The Exception we're going to throw */
    private E exception;
    
    /**
     * Creates an {@link AsyncExchanger} with the default configuration.
     */
    public AsyncExchanger() {
        this(THIS);
    }
    
    /**
     * Creates an {@link AsyncExchanger} that uses the given lock Object
     */
    public AsyncExchanger(Object lock) {
        if (lock == null) {
            throw new NullArgumentException("lock");
        }
        
        this.lock = (lock != THIS) ? lock : this;     
    }
    
    /**
     * Returns the lock Object
     */
    public Object getLock() {
        return lock;
    }
    
    /**
     * Waits for another Thread for a value or an Exception
     * unless they're already set in which case this method
     * will return immediately.
     */
    public V get() throws InterruptedException, E {
        synchronized (lock) {
            try {
                return get(0L, TimeUnit.MILLISECONDS);
            } catch (TimeoutException cannotHappen) {
                throw new Error(cannotHappen);
            }
        }
    }
    
    /**
     * Waits for another Thread for the given time for a value 
     * or an Exception unless they're already set in which case 
     * this method will return immediately.
     */
    public V get(long timeout, TimeUnit unit) 
            throws InterruptedException, TimeoutException, E {
        
        synchronized (lock) {
            if (!done) {
                if (timeout == 0L) {
                    lock.wait();
                } else {
                    unit.timedWait(lock, timeout);
                }
                
                // Not done? Must be a timeout!
                if (!done) {
                    throw new TimeoutException();
                }
            }
            
            if (cancelled) {
                throw new CancellationException();
            }
            
            // Prioritize Exceptions!
            if (exception != null) {
                throw exception;
            }
            
            return value;
        }
    }
    
    /**
     * Tries to get the value without blocking.
     */
    public V tryGet() throws InterruptedException, E {
        synchronized (lock) {
            if (done) {
                return get();
            } else {
                return null;
            }
        }
    }
    
    /**
     * Tries to cancel the {@link AsyncExchanger} and returns true
     * on success.
     */
    public boolean cancel() {
        synchronized (lock) {
            if (done) {
                return cancelled;
            }
            
            done = true;
            cancelled = true;
            
            lock.notifyAll();
            return true;
        }
    }
    
    /**
     * Returns true if the {@link AsyncExchanger} is canceled
     */
    public boolean isCancelled() {
        synchronized (lock) {
            return cancelled;
        }
    }
    
    /**
     * Returns true if the get() method will return immediately
     * by throwing an Exception or returning a value
     */
    public boolean isDone() {
        synchronized (lock) {
            return done;
        }
    }
    
    /**
     * Returns true if calling the get() method will
     * throw an Exception
     */
    public boolean isCompletedAbnormally() {
        synchronized (lock) {
            return cancelled || exception != null;
        }
    }
    
    /**
     * Sets the value that will be returned by the get() method
     */
    public boolean setValue(V value) {
        synchronized (lock) {
            if (done || cancelled) {
                return false;
            }
            
            done = true;
            this.value = value;
            lock.notifyAll();
            return true;
        }
    }
    
    /**
     * Sets the Exception that will be thrown by the get() method
     */
    public boolean setException(E exception) {
        if (exception == null) {
            throw new NullArgumentException("exception");
        }
        
        synchronized (lock) {
            if (done || cancelled) {
                return false;
            }
            
            done = true;
            this.exception = exception;
            lock.notifyAll();
            return true;
        }
    }
    
    /**
     * Resets the {@link AsyncExchanger}
     */
    public boolean reset() {
        synchronized (lock) {
            if (done || cancelled) {
                done = false;
                cancelled = false;
                value = null;
                exception = null;
                return true;
            }
            
            return false;
        }
    }
    
    @Override
    public String toString() {
        boolean done = false;
        boolean cancelled = false;
        V value = null;
        E exception = null;
        
        synchronized (lock) {
            done = this.done;
            cancelled = this.cancelled;
            value = this.value;
            exception = this.exception;
        }
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("AsyncExchanger: ")
            .append("done=").append(done)
            .append(", cancelled=").append(cancelled)
            .append(", value=").append(value)
            .append(", exception=").append(ExceptionUtils.toString(exception));
        return buffer.toString();
    }
}
