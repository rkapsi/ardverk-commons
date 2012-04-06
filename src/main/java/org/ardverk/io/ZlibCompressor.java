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

package org.ardverk.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;

import org.ardverk.lang.MathUtils;

/**
 * An implementation of {@link Compressor} that uses Java's 
 * {@link InflaterInputStream} and {@link DeflaterOutputStream}.
 */
public class ZlibCompressor extends AbstractCompressor {

  public static final ZlibCompressor ZLIB = new ZlibCompressor();
  
  private ZlibCompressor() {
    super("ZLIB");
  }
  
  @Override
  public byte[] compress(byte[] value, int offset, int length)
      throws IOException {
    
    ByteArrayOutputStream baos = null;
    OutputStream out = null;
    
    try {
      baos = new ByteArrayOutputStream(MathUtils.nextPowOfTwo(length));
      out = new DeflaterOutputStream(baos);   
      out.write(value, offset, length);
    } finally {
      IoUtils.closeAll(out, baos);
    }
    
    return baos.toByteArray();
  }

  @Override
  public byte[] decompress(byte[] value, int offset, int length)
      throws IOException {
    ByteArrayOutputStream baos = null;
    OutputStream out = null;
    try {
      baos = new ByteArrayOutputStream(MathUtils.nextPowOfTwo(2 * length));
      out = new InflaterOutputStream(baos);
      out.write(value, offset, length);
    } finally {
      IoUtils.closeAll(out, baos);
    }
    
    return baos.toByteArray();
  }
}