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

package org.ardverk.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CollectionsUtils {

    private CollectionsUtils() {}
    
    /**
     * Copies src to dst if src is not null and not empty. It 
     * will create and return a new Map if dst is either null 
     * or {@link Collections#EMPTY_MAP}.
     */
    public static Map<Object, Object> copyTo(
            Map<?, ?> src, Map<Object, Object> dst) {
        
        if (src != null && !src.isEmpty()) {
            if (dst == null || dst.equals(Collections.EMPTY_MAP)) {
                dst = new HashMap<Object, Object>(src);
            } else {
                dst.putAll(src);
            }
        }
        
        return dst;
    }
}
