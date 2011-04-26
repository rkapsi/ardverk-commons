package org.ardverk.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class NopInputStream extends InputStream {
    
    private boolean eof = false;

    private boolean open = true;
    
    @Override
    public int read() throws IOException {
        if (!open) {
            throw new IOException();
        }
        
        if (eof) {
            throw new EOFException();
        }
        
        eof = true;
        return -1;
    }

    @Override
    public void close() {
        open = false;
    }
}