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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An implementation of {@link FilterInputStream} that is being notified 
 * if the {@link InputStream} is being closed or has reached its EOF.
 */
public class CloseAwareInputStream extends FilterInputStream {

    private final AtomicBoolean open = new AtomicBoolean(true);
    
    public CloseAwareInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int value = -1;
        try {
            value = super.read();
        } finally {
            complete0(value);
        }
        return value;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int value = -1;
        try {
            value = super.read(b);
        } finally {
            complete0(value);
        }
        return value;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int value = -1;
        try {
            value = super.read(b, off, len);
        } finally {
            complete0(value);
        }
        return value;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            complete0(-1);
        }
    }
    
    private void complete0(int value) {
        if (value < 0 && open.getAndSet(false)) {
            complete();
        }
    }
    
    /**
     * This method is called if the {@link CloseAwareInputStream} was 
     * closed or reached its EOF.
     */
    protected void complete() {
    }
}
