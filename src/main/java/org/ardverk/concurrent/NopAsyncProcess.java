package org.ardverk.concurrent;

/**
 * An implementation of {@link AsyncProcess} that does nothing.
 */
public class NopAsyncProcess<V> implements AsyncProcess<V> {

    private static final AsyncProcess<Object> NOP 
        = new NopAsyncProcess<Object>();
    
    @SuppressWarnings("unchecked")
    public static <V> AsyncProcess<V> create() {
        return (AsyncProcess<V>)NOP;
    }
    
    private NopAsyncProcess() {}
    
    @Override
    public void start(AsyncProcessFuture<V> future) {
    }
}
