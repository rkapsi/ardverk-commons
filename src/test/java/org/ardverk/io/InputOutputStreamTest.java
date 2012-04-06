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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Test;

public class InputOutputStreamTest {

  @Test
  public void produce() throws IOException {
    final byte[] expected = new byte[8*1024];

    Random generator = new Random();
    generator.nextBytes(expected);
    
    InputStream in = new InputOutputStream() {
      @Override
      protected void produce(OutputStream out) throws IOException {
        out.write(expected);
      }
    };
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    byte[] buffer = new byte[4*1024];
    int len = -1;
    while ((len = in.read(buffer)) != -1) {
      baos.write(buffer, 0, len);
    }
    baos.close();
    in.close();
    
    byte[] actual = baos.toByteArray();
    TestCase.assertEquals(expected.length, actual.length);
    
    for (int i = 0; i < expected.length; i++) {
      TestCase.assertEquals(expected[i], actual[i]);
    }
  }
}
