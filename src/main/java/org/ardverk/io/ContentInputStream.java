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
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContentInputStream extends FilterInputStream {
    
    private final long length;
    
    private long position = 0L;
    
    private boolean open = true;
    
    private boolean eof = false;
    
    public ContentInputStream(InputStream in, long length) {
        super(in);
        this.length = length;
    }
    
    public long position() {
        return position;
    }
    
    public long length() {
        return length;
    }
    
    @Override
    public int read() throws IOException {
        if (!open) {
            throw new IOException();
        }
        
        if (eof) {
            throw new EOFException();
        }
        
        int value = -1;
        try {
            if (0L < remaining()) {
                value = super.read();
            }
        } finally {
            check(value, 1);
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
        
        int r = -1;
        try {
            long remaining = remaining();
            if (0L < remaining) {
                r = super.read(b, off, (int)Math.min(len, remaining));
            }
        } finally {
            check(r, r);
        }
        
        return r;
    }
    
    private void check(int r, int len) {
        if (r == -1) {
            eof = true;
        } else {
            position += len;
        }
    }

    /**
     * Returns the number of bytes remaining in this {@link InputStream} or
     * {@link Long#MAX_VALUE} if it's unknown.
     */
    public long remaining() {
        if (open && !eof) {
            if (length == -1L) {
                return Long.MAX_VALUE;
            }
            
            return length-position;
        }
        return 0L;
    }
    
    @Override
    public int available() throws IOException {
        if (!open || eof) {
            throw new IOException();
        }
        
        long remaining = remaining();
        return (int)Math.min(remaining, Integer.MAX_VALUE);
    }
    
    @Override
    public void close() throws IOException {
        close(false);
    }
    
    public void close(boolean forceClose) throws IOException {
        close(forceClose, false);
    }
    
    public void close(boolean forceClose, boolean skipRemaining) throws IOException {
        if (open) {
            try {
                if (length != -1L) {
                    long remaining = remaining();
                    if (skipRemaining && 0L < remaining) {
                        skip(remaining);
                    }
                }
            } finally {
                open = false;
                
                if (forceClose) {
                    super.close();
                }
            }
        }
    }
}
