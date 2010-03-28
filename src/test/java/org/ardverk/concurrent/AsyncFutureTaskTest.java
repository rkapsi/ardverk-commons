package org.ardverk.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.TestCase;

import org.junit.Test;

public class AsyncFutureTaskTest {

    private static AsyncProcess<String> NOP = new AsyncProcess<String>() {
        @Override
        public void start(AsyncFuture<String> future) {
            future.setValue("Hello World!");
        }
    };
    
    @Test
    public void addListener() 
            throws InterruptedException, ExecutionException {
        AsyncExecutorService executor = AsyncExecutors.newSingleThreadExecutor(
                10L, TimeUnit.SECONDS);

        final CountDownLatch latch = new CountDownLatch(1);
        AsyncFuture<String> future = executor.submit(NOP);
        future.addAsyncFutureListener(new AsyncFutureListener<String>() {
            @Override
            public void operationComplete(AsyncFuture<String> future) {
                latch.countDown();
            }
        });
        
        TestCase.assertTrue(latch.await(1L, TimeUnit.SECONDS));
        
        String value = future.get();
        TestCase.assertTrue(future.isDone());
        TestCase.assertEquals("Hello World!", value);
    }
    
    @Test
    public void addListenerBeforeCompletion() 
            throws InterruptedException, ExecutionException {
        AsyncExecutorService executor = AsyncExecutors.newSingleThreadExecutor(
                10L, TimeUnit.SECONDS);

        final CountDownLatch latch = new CountDownLatch(1);
        AsyncFuture<String> future = null;
        synchronized (this) {
            AsyncProcess<String> process = new AsyncProcess<String>() {
                @Override
                public void start(AsyncFuture<String> future) {
                    synchronized (AsyncFutureTaskTest.this) {
                        future.setValue("Hello World!");
                    }
                }
            };
            
            future = executor.submit(process);
            
            synchronized (future) {
                TestCase.assertFalse(future.isDone());
                
                future.addAsyncFutureListener(new AsyncFutureListener<String>() {
                    @Override
                    public void operationComplete(AsyncFuture<String> future) {
                        latch.countDown();
                    }
                });
            }
        }
        
        TestCase.assertTrue(latch.await(1L, TimeUnit.SECONDS));
        
        String value = future.get();
        TestCase.assertTrue(future.isDone());
        TestCase.assertEquals("Hello World!", value);
    }
    
    @Test
    public void addListenerAfterCompletion() 
            throws InterruptedException, ExecutionException {
        AsyncExecutorService executor = AsyncExecutors.newSingleThreadExecutor(
                10L, TimeUnit.SECONDS);

        AsyncFuture<String> future = executor.submit(NOP);
        
        String value = future.get();
        TestCase.assertTrue(future.isDone());
        TestCase.assertEquals("Hello World!", value);
        
        final CountDownLatch latch = new CountDownLatch(1);
        future.addAsyncFutureListener(new AsyncFutureListener<String>() {
            @Override
            public void operationComplete(AsyncFuture<String> future) {
                latch.countDown();
            }
        });
        
        TestCase.assertTrue(latch.await(1L, TimeUnit.SECONDS));
    }
    
    @Test
    public void execute() throws InterruptedException, 
            ExecutionException, TimeoutException {
        AsyncExecutorService executor = AsyncExecutors.newSingleThreadExecutor(
                10L, TimeUnit.SECONDS);
        
        AsyncProcess<String> process = new AsyncTask();
        AsyncFuture<String> future = executor.submit(process);
        
        String value = future.get(1L, TimeUnit.SECONDS);
        
        TestCase.assertEquals("Hello World", value);
    }
    
    @Test
    public void timeout() throws InterruptedException, TimeoutException {
        AsyncExecutorService executor = AsyncExecutors.newSingleThreadExecutor(
                250L, TimeUnit.MILLISECONDS);
        
        AsyncProcess<String> process = new AsyncProcess<String>() {
            @Override
            public void start(AsyncFuture<String> future) throws Exception {
                // DO NOTHING
            }
        };
        
        AsyncFuture<String> future = executor.submit(process);
        try {
            future.get(1L, TimeUnit.SECONDS);
            TestCase.fail("Should have failed!");
        } catch (ExecutionException expected) {
            TestCase.assertTrue(
                    isCausedBy(expected, TimeoutException.class));
        } catch (TimeoutException expected) {
            
        }
    }
    
    @Test
    public void checkDefaultConstructor() 
            throws InterruptedException, TimeoutException {
        Executor executor = Executors.newSingleThreadExecutor();
        
        AsyncFutureTask<String> future 
            = new AsyncFutureTask<String>();
        
        executor.execute(future);
        
        try {
            future.get(1L, TimeUnit.SECONDS);
            TestCase.fail("Should have failed!");
        } catch (ExecutionException expected) {
            TestCase.assertTrue(
                    isCausedBy(expected, IllegalStateException.class));
        }
    }
    
    private static class AsyncTask implements AsyncProcess<String> {

        private final Executor executor = Executors.newSingleThreadExecutor();
        
        @Override
        public void start(final AsyncFuture<String> future) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    future.setValue("Hello World");
                }
            };
            
            executor.execute(task);
        }
        
    }
    
    public static boolean isCausedBy(Throwable t, Class<? extends Throwable> clazz) {
        return getCause(t, clazz) != null;
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T getCause(Throwable t, Class<T> clazz) {
        while(t != null) {
            if (clazz.isInstance(t)) {
                return (T)t;
            }
            t = t.getCause();
        }
        return null;
    }
}
