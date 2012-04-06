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

package org.ardverk.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

  public TimeUtils() {}
  
  /**
   * Converts the given time and {@link TimeUnit} to an another {@link TimeUnit}.
   * 
   * TimeUtils.convert(5L, TimeUnit.MINUTS, TimeUnit.MILLISECONDS)
   * TimeUnit.MILLISECONDS.convert(5L, TimeUnit.MINUTES)
   */
  public static long convert(long time, TimeUnit src, TimeUnit dst) {
    return dst.convert(time, src);
  }
}