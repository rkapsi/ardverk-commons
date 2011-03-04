package org.ardverk.version;

public interface Version<T> {

    public static enum Occured {
        BEFORE(0),
        EQUAL(0),
        AFTER(1),
        CONCURRENTLY(2);
        
        private final int value;
        
        private Occured(int value) {
            this.value = value;
        }
        
        public int intValue() {
            return value;
        }
    }
    
    public Occured compareTo(T other);
}
