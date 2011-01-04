/*
 * Copyright 2010-2011 Roger Kapsi
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

import junit.framework.TestCase;

import org.junit.Test;

public class ZipperTest {

    @Test
    public void add() {
        Zipper<String> list = Zipper.create();
        list = list.add("Hello").add("World");
        
        TestCase.assertFalse(list.isEmpty());
        TestCase.assertEquals(2, list.size());
        
        TestCase.assertTrue(list.contains("Hello"));
        TestCase.assertTrue(list.contains("World"));
        TestCase.assertFalse(list.contains("Foo Bar"));
    }
}