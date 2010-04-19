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

package org.ardverk.lang;

public class NumberUtils {

    private NumberUtils() {}
    
    /**
     * Returns true if the given value is undefined in the sense that
     * {@link Double#isNaN(double)} or {@link Double#isInfinite(double)}
     * would return true.
     */
    public static boolean isUndefined(double value) {
        return Double.isNaN(value) || Double.isInfinite(value);
    }
    
    /**
     * Returns true if the given value is undefined in the sense that
     * {@link Float#isNaN(double)} or {@link Float#isInfinite(double)}
     * would return true.
     */
    public static boolean isUndefined(float value) {
        return Float.isNaN(value) || Float.isInfinite(value);
    }
}
