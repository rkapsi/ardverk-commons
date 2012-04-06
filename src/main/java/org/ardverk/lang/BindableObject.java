package org.ardverk.lang;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.ardverk.concurrent.EventUtils;

public class BindableObject<T> implements Bindable<T> {

  private final List<BindingListener<T>> listeners 
    = new CopyOnWriteArrayList<BindingListener<T>>();
  
  private T t = null;
  
  public synchronized T object() {
    return t;
  }
  
  @Override
  public synchronized void bind(T t) {
    if (t == null) {
      throw new NullPointerException("t");
    }
    
    if (this.t != null) {
      throw new IllegalStateException();
    }
    
    this.t = t;
    fireBind(t);
  }

  @Override
  public synchronized boolean isBound() {
    return t != null;
  }

  @Override
  public synchronized void unbind() {
    if (t != null) {
      T t = this.t;
      this.t = null;
      
      fireUnbind(t);
    }
  }

  public void addBindingListener(BindingListener<T> l) {
    listeners.add(l);
  }

  public void removeBindingListener(BindingListener<T> l) {
    listeners.remove(l);
  }

  @SuppressWarnings("unchecked")
  public BindingListener<T>[] getBindingListeners() {
    return listeners.toArray(new BindingListener[0]);
  }

  protected void fireBind(final T t) {
    if (!listeners.isEmpty()) {
      Runnable event = new Runnable() {
        @Override
        public void run() {
          for (BindingListener<T> l : listeners) {
            l.handleBind(t);
          }
        }
      };
      
      EventUtils.fireEvent(event);
    }
  }
  
  protected void fireUnbind(final T t) {
    if (!listeners.isEmpty()) {
      Runnable event = new Runnable() {
        @Override
        public void run() {
          for (BindingListener<T> l : listeners) {
            l.handleUnbind(t);
          }
        }
      };
      
      EventUtils.fireEvent(event);
    }
  }
  
  /**
   * 
   */
  public static interface BindingListener<T> {
    
    /**
     * 
     */
    public void handleBind(T t);
    
    /**
     * 
     */
    public void handleUnbind(T t);
  }
}
