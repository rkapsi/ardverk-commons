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

package org.ardverk.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.ardverk.lang.MathUtils;

/**
 * An implementation of {@link Compressor} that uses Java's 
 * {@link GZIPInputStream} and {@link GZIPOutputStream}.
 */
public class GzipCompressor extends AbstractCompressor {

    public static final GzipCompressor GZIP = new GzipCompressor();
    
    private GzipCompressor() {
        super("GZIP");
    }
    
    @Override
    public byte[] compress(byte[] value, int offset, int length)
            throws IOException {
        
        ByteArrayOutputStream baos = null;
        OutputStream out = null;
        
        try {
            baos = new ByteArrayOutputStream(MathUtils.nextPowOfTwo(length));
            out = new GZIPOutputStream(baos);   
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
        try {
            baos = new ByteArrayOutputStream(MathUtils.nextPowOfTwo(2 * length));
            
            ByteArrayInputStream bais = null;
            InputStream in = null;
            
            try {
                bais = new ByteArrayInputStream(value, offset, length);
                in = new GZIPInputStream(bais);
                
                byte[] buffer = new byte[Math.min(length, 1024)];
                int len = -1;
                
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                
            } finally {
                IoUtils.closeAll(in, bais);
            }
            
        } finally {
            IoUtils.close(baos);
        }
        
        return baos.toByteArray();
    }
}

