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

package org.ardverk.lang;

import java.util.Comparator;

public class Longs {
  
  private Longs() {}

  /**
   * Compares the given {@code long} values.
   * 
   * @see Comparable
   * @see Comparator
   */
  public static int compare(long l1, long l2) {
    if (l1 < l2) {
      return -1;
    } else if (l2 < l1) {
      return 1;
    }
    return 0;
  }
}