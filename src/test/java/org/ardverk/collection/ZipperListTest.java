package org.ardverk.collection;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

public class ZipperListTest {

    @Test
    public void addConcurrently() throws InterruptedException {
        final Collection<String> c = new ZipperList<String>();
        
        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    c.add("Value: " + i);
                }
            }
        };
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            executor.execute(task);
        }
        
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        
        TestCase.assertEquals(10*1000, c.size());
    }
}
