/*
 * Copyright 2010-2012 Roger Kapsi
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

package org.ardverk.security.token;

import java.io.Closeable;
import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.ardverk.concurrent.ExecutorUtils;
import org.ardverk.concurrent.FutureUtils;
import org.ardverk.security.SecurityUtils;

/**
 * A default implementation of {@link SecurityToken}.
 */
public class DefaultSecurityToken extends AbstractSecurityToken implements Closeable {

  private static final ScheduledExecutorService EXECUTOR 
    = ExecutorUtils.newSingleThreadScheduledExecutor("SecurityTokenThread");
  
  private static final Random RANDOM = SecurityUtils.createSecureRandom();
  
  private final Object messageDigestLock = new Object();
  
  private final MessageDigest messageDigest;
  
  private final ScheduledFuture<?> future;
  
  private final Object keyLock = new Object();
  
  private byte[] current = null;
  
  private byte[] previous = null;
  
  public DefaultSecurityToken(MessageDigest messageDigest, 
      long frequency, TimeUnit unit) {
    this(EXECUTOR, messageDigest, RANDOM, frequency, unit);
  }
  
  public DefaultSecurityToken(ScheduledExecutorService executor, 
      MessageDigest messageDigest, final Random random, 
      long frequency, TimeUnit unit) {
    
    this.messageDigest = messageDigest;
    
    // TODO: Hmm, is this sufficient?
    current = new byte[messageDigest.getDigestLength()];
    random.nextBytes(current);
    
    ScheduledFuture<?> future = null;
    if (0 < frequency) {
      Runnable task = new Runnable() {
        @Override
        public void run() {
          synchronized (keyLock) {
            previous = current;
            
            // Create a new byte[] of the same length
            current = new byte[current.length];
            random.nextBytes(current);
          }
        }
      };
      
      future = executor.scheduleWithFixedDelay(
          task, frequency, frequency, unit);
    }
    
    this.future = future;
  }
  
  @Override
  public void close() {
    FutureUtils.cancel(future, true);
  }
  
  @Override
  public byte[] create(byte[] data, int offset, int length) {
    
    byte[] current = null;
    synchronized (keyLock) {
      current = this.current;
    }
    
    return create(current, data, offset, length);
  }
  
  private byte[] create(byte[] key, byte[] data, 
      int offset, int length) {
    
    synchronized (messageDigestLock) {
      messageDigest.reset();
      
      messageDigest.update(key);
      messageDigest.update(data, offset, length);
      
      return messageDigest.digest();
    }
  }
  
  @Override
  public boolean isFor(byte[] data, int dataOffset, int dataLength, 
      byte[] securityToken, int offset, int length) {
    
    byte[] current = null;
    byte[] previous = null;
    
    synchronized (keyLock) {
      current = this.current;
      previous = this.previous;
    }
    
    if (current != null) {
      byte[] expected = create(current, data, dataOffset, dataLength);
      if (equals(expected, securityToken, offset, length)) {
        return true;
      }
      
      if (previous != null) {
        expected = create(previous, data, dataOffset, dataLength);
        return equals(expected, securityToken, offset, length);
      }
    }
    
    return false;
  }
  
  private static boolean equals(byte[] expected, 
      byte[] actual, int offset, int length) {
    
    if (expected.length == length) {
      for (int i = 0; i < length; i++) {
        if (expected[i] != actual[offset + i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}
