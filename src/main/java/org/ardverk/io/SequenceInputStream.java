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

package org.ardverk.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Same as {@link java.io.SequenceInputStream} but that's not using 
 * {@link Enumeration}s.
 * 
 * @see java.io.SequenceInputStream
 */
public class SequenceInputStream extends InputStream {

    private final InputStream[] streams;
    
    private int index = 0;
    
    private InputStream in = null;
    
    private boolean eof = false;
    
    private boolean open = true;
    
    public SequenceInputStream(InputStream in, 
            InputStream... streams) {
        
        if (in == null) {
            throw new NullPointerException();
        }
        
        this.in = in;
        this.streams = streams;
    }

    private void advance() throws IOException {
        // Close the current InputStream
        if (in != null) {
            in.close();
        }
        
        // Get the next InputStream
        this.in = next();
    }
    
    private InputStream next() {
        InputStream in = null;
        if (index < streams.length) {
            in = streams[index++];
            if (in == null) {
                throw new NullPointerException();
            }
        }
        return in;
    }
    
    @Override
    public int available() throws IOException {
        if (in == null) {
            return 0;
        }
        return in.available();
    }

    @Override
    public int read() throws IOException {
        if (!open) {
            throw new IOException();
        }
        
        if (eof) {
            throw new EOFException();
        }
        
        if (in == null) {
            eof = true;
            return -1;
        }
        
        int value = in.read();
        if (value < 0) {
            advance();
            return read();
        }
        
        return value;
    }
    
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!open) {
            throw new IOException();
        }
        
        if (eof) {
            throw new EOFException();
        }
        
        if (in == null) {
            eof = true;
            return -1;
        }
        
        int n = in.read(b, off, len);
        if (n < 0) {
            advance();
            return read(b, off, len);
        }
        return n;
    }

    @Override
    public void close() throws IOException {
        if (open) {
            open = false;
            
            do {
                advance();
            } while (in != null);
        }
    }
}
