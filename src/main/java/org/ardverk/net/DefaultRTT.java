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

package org.ardverk.net;

import java.util.concurrent.TimeUnit;

/**
 * Jacobson + Karel's Algorithm
 */
public class DefaultRTT implements RTT {

    public static final double A = 0.8;
    
    public static final int P = 1;
    
    public static final int Q = 4;
    
    private final double a;
    
    private final double delta;
    
    private final int p;
    
    private final int q;
    
    private double ertt = 0;
    
    private double drtt = 0;
    
    private long rtt = 0;
    
    private int timeout = 1;
    
    public DefaultRTT() {
        this(A, P, Q);
    }
    
    public DefaultRTT(long rtt, TimeUnit unit) {
        this(A, P, Q, rtt, unit);
    }
    
    public DefaultRTT(double a, int p, int q) {
        this(a, p, q, 0L, TimeUnit.MILLISECONDS);
    }
    
    public DefaultRTT(double a, int p, int q, 
            long rtt, TimeUnit unit) {
        
        if (a < 0 || 1 < a) {
            throw new IllegalArgumentException("a=" + a);
        }
        
        if (p < 0) {
            throw new IllegalArgumentException("p=" + p);
        }
        
        if (q < 0) {
            throw new IllegalArgumentException("q=" + q);
        }
        
        this.a = a;
        this.delta = 1 - a;
        
        this.p = p;
        this.q = q;
        
        setRoundTripTime(rtt, unit);
    }
    
    @Override
    public synchronized void setRoundTripTime(long rtt, TimeUnit unit) {
        long time = unit.toMillis(rtt);
        
        if (0 < time) {
            this.timeout = 1;
            
            this.ertt = (a * ertt) + (delta * time);
            this.drtt = (a * drtt) + (delta * Math.abs(ertt - time));
            this.rtt = (long)((p * ertt + q * drtt) + 0.5d);
        }
    }
    
    @Override
    public synchronized long getRoundTripTime(TimeUnit unit) {
        return unit.convert(rtt * timeout, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public synchronized long getRoundTripTimeInMillis() {
        return getRoundTripTime(TimeUnit.MILLISECONDS);
    }
    
    @Override
    public synchronized void timeout() {
        timeout *= 2;
    }
    
    @Override
    public String toString() {
        return getRoundTripTimeInMillis() + "ms";
    }
}
