package org.ardverk.lang;

public class NullArgumentException extends NullPointerException {
    
    private static final long serialVersionUID = 8784890523282411156L;

    public NullArgumentException() {
        super();
    }

    public NullArgumentException(String s) {
        super(s);
    }
}
