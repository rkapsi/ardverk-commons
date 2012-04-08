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

import java.util.EnumSet;

import org.ardverk.utils.ArrayUtils;

public class EnumUtils {

  public EnumUtils() {}
  
  /**
   * Creates and returns an {@link EnumSet}
   */
  @SafeVarargs
  public static <E extends Enum<E>> EnumSet<E> createSet(Class<E> clazz, E... elements) {
    switch (elements.length) {
      case 0:
        return EnumSet.noneOf(clazz);
      case 1:
        return EnumSet.of(elements[0]);
      case 2:
        return EnumSet.of(elements[0], elements[1]);
      case 3:
        return EnumSet.of(elements[0], elements[1], elements[2]);
      case 4:
        return EnumSet.of(elements[0], elements[1], elements[2], elements[3]);
      default:
        E first = elements[0];
        E[] others = ArrayUtils.newInstance(clazz, elements.length-1);
        System.arraycopy(elements, 1, others, 0, others.length);
        return EnumSet.of(first, others);
    }
  }
}
