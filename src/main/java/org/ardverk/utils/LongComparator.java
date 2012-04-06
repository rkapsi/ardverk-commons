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

import java.io.Serializable;
import java.util.Comparator;

import org.ardverk.lang.Longs;

/**
 * A {@link Comparator} for long values.
 */
public class LongComparator implements Comparator<Long>, Serializable {
  
  private static final long serialVersionUID = -611530289701062817L;

  public static final LongComparator COMPARATOR = new LongComparator();
  
  @Override
  public int compare(Long o1, Long o2) {
    return Longs.compare(o1.longValue(), o2.longValue());
  }
}