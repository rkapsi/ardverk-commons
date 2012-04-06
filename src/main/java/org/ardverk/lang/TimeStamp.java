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

package org.ardverk.lang;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * The {@link TimeStamp} represents a point in time. It's main purpose is to 
 * provide methods to measure elapsed time and fix various problems we're 
 * seeing with {@link System#currentTimeMillis()} and {@link System#nanoTime()}.
 * 
 * <p><ol>
 * <li>The system time as returned by {@link System#currentTimeMillis()} may 
 * jump back and forth if the user or a system such as the Network Time Protocol
 * (NTP) messes with the computer's clock.
 * 
 * <li>The time as returned by {@link System#nanoTime()} is prone to race 
 * conditions because modern multi-core CPUs may have a high precision clock 
 * for each core and they're not 100% in-sync with each other.
 * </ol>
 * 
 * I'm not sure if the first issue is immune to the second problem and both 
 * issues may lead to negative time measurements which is something that we're 
 * usually not expecting at all when working time. The following two examples 
 * demonstrate the problems.
 * 
 * <pre>
 * long startTime = System.currentTimeMillis();
 * // Do something and the computer's clock changes in the mean time
 * long elapsedTime = System.currentTimeMillis() - startTime;
 * assert (elapsedTime >= 0L);
 * </pre>
 * 
 * <pre>
 * final long startTime = System.nanoTime();
 * new Thread(new Runnable() {
 *    @Override
 *    public void run() {
 *      // Do something and this is hopefully running on a different
 *      // CPU core than the one that captured the startTime.
 *      long elapsedTime = System.nanoTime() - startTime;
 *      assert (elapsedTime >= 0L);
 *    }
 * }).start();
 * </pre>
 * 
 * We fix the problem by ignoring it and letting {@link #getAge(TimeUnit)}
 * always return a non-negative value. The returned value is maybe inaccurate
 * but it's at least not negative.
 */
public class TimeStamp implements Epoch, Age, Comparable<TimeStamp>, Serializable {
  
  private static final long serialVersionUID = -981788126324372167L;

  /**
   * Weather not {@link #getAge(TimeUnit)} will return the absolute time.
   * 
   * @see Math#abs(long)
   */
  private static final boolean ABSOLUTE = true;
  
  /**
   * Creates and returns a {@link TimeStamp}.
   */
  public static TimeStamp now() {
    return new TimeStamp();
  }
  
  private final long timeStamp = System.currentTimeMillis();
  
  private TimeStamp() {}
  
  @Override
  public long getCreationTime() {
    return timeStamp;
  }
  
  @Override
  public long getAge(TimeUnit unit) {
    long time = System.currentTimeMillis() - timeStamp;
    return unit.convert(check(time), TimeUnit.MILLISECONDS);
  }
  
  @Override
  public long getAgeInMillis() {
    return getAge(TimeUnit.MILLISECONDS);
  }
  
  @Override
  public int compareTo(TimeStamp other) {
    return Longs.compare(timeStamp, other.timeStamp);
  }
  
  @Override
  public int hashCode() {
    return (int)timeStamp;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof TimeStamp)) {
      return false;
    }
    
    TimeStamp other = (TimeStamp)o;
    return timeStamp == other.timeStamp;
  }
  
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("Creation Time: ").append(new Date(getCreationTime()))
      .append(", Age=").append(getAgeInMillis()).append("ms");
    return buffer.toString();
  }
  
  /**
   * Checks if {@link #ABSOLUTE} is {@code true} and returns the 
   * {@link Math#abs(long)} value of the given argument.
   */
  private static long check(long value) {
    return ABSOLUTE ? Math.abs(value) : value;
  }
}