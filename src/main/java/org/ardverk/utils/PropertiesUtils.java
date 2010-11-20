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

package org.ardverk.utils;

public class PropertiesUtils {

    private PropertiesUtils() {}
    
    public static String createKey(Class<?> clazz, String name) {
        return clazz.getCanonicalName() + "." + name;
    }
    
    public static int getInteger(Class<?> clazz, String name, int defaultValue) {
        return getInteger(createKey(clazz, name), defaultValue);
    }
    
    public static int getInteger(String key, int defaultValue) {
        return Integer.parseInt(getProperty(key, Integer.toString(defaultValue)));
    }
    
    public static long getLong(Class<?> clazz, String name, long defaultValue) {
        return getLong(createKey(clazz, name), defaultValue);
    }
    
    public static long getLong(String key, long defaultValue) {
        return Long.parseLong(getProperty(key, Long.toString(defaultValue)));
    }
    
    public static String getProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}
