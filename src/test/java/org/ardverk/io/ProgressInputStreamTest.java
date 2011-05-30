package org.ardverk.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.ardverk.io.ProgressInputStream.ProgressAdapter;
import org.ardverk.io.ProgressInputStream.ProgressCallback;
import org.junit.Test;

public class ProgressInputStreamTest {

    @Test
    public void close() throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        ProgressCallback callback = new ProgressAdapter() {
            @Override
            public void closed(InputStream in) {
                latch.countDown();
            }
        };
        
        ProgressInputStream in = new ProgressInputStream(
                new ByteArrayInputStream(new byte[0]), callback);
        
        in.close();
        
        if (!latch.await(1, TimeUnit.SECONDS)) {
            TestCase.fail("Close failed!");
        }
    }
    
    @Test
    public void eof() throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        ProgressCallback callback = new ProgressAdapter() {
            @Override
            public void eof(InputStream in) {
                latch.countDown();
            }
        };
        
        ProgressInputStream in = new ProgressInputStream(
                new ByteArrayInputStream(new byte[0]), callback);
        
        int r = in.read();
        TestCase.assertEquals(-1, r);
        
        if (!latch.await(1, TimeUnit.SECONDS)) {
            TestCase.fail("Close failed!");
        }
    }
}
