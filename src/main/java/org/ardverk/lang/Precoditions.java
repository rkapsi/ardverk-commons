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

/**
 * An utility class to check arguments.
 */
public class Precoditions {

  private Precoditions() {}
  
  public static void argument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }
  
  public static void argument(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void argument(boolean expression, 
      String message, Object... arguments) {
    if (!expression) {
      throw new IllegalArgumentException(String.format(message, arguments));
    }
  }

  /**
   * Makes sure the given argument is not {@code null}.
   */
  public static <T> T notNull(T t) {
    return notNull(t, null);
  }
  
  /**
   * Makes sure the given argument is not {@code null}.
   */
  public static <T> T notNull(T t, String message) {
    if (t == null) {
      if (message != null) {
        throw new NullPointerException(message);
      }
      throw new NullPointerException();
    }
    return t;
  }
}
