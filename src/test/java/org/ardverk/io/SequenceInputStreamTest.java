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

package org.ardverk.io;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

public class SequenceInputStreamTest {

  @Test
  public void read() throws IOException {
    ByteArrayInputStream in1 
      = new ByteArrayInputStream(new byte[] { 1 });
    
    ByteArrayInputStream in2 
      = new ByteArrayInputStream(new byte[] { 2 });
    
    SequenceInputStream in 
      = new SequenceInputStream(in1, in2);
    
    TestCase.assertEquals(1, in.read());
    TestCase.assertEquals(2, in.read());
    
    TestCase.assertEquals(-1, in.read());
    
    try {
      in.read();
      TestCase.fail("Should have failed!");
    } catch (EOFException expected) {}
  }
}
