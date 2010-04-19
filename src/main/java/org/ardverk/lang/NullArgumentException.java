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

public class NullArgumentException extends NullPointerException {
    
    private static final long serialVersionUID = 8784890523282411156L;

    public static <T> T notNull(T value, String message) {
        if (value == null) {
            throw new NullArgumentException(message);
        }
        
        return value;
    }
    
    public NullArgumentException() {
        super();
    }

    public NullArgumentException(String s) {
        super(s);
    }
}
