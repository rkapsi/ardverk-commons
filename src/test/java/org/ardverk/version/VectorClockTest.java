package org.ardverk.version;

import junit.framework.TestCase;

import org.junit.Test;

public class VectorClockTest {

    @Test
    public void equal1() {
        VectorClock<String> clock = new VectorClock<String>();
        clock.add("roger");
        
        TestCase.assertEquals(Occured.BEFORE, clock.compareTo(clock));
    }
    
    @Test
    public void concurrently1() {
        VectorClock<String> clock1 = new VectorClock<String>();
        clock1.add("roger");
        
        VectorClock<String> clock2 = new VectorClock<String>();
        clock2.add("odvar");
        
        TestCase.assertEquals(Occured.CONCURRENTLY, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.CONCURRENTLY, clock2.compareTo(clock1));
    }
    
    @Test
    public void after1() {
        VectorClock<String> clock1 = new VectorClock<String>();
        clock1.add("roger");
        clock1.add("odvar");
        
        VectorClock<String> clock2 = new VectorClock<String>();
        clock2.add("odvar");
        
        TestCase.assertEquals(Occured.AFTER, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.BEFORE, clock2.compareTo(clock1));
    }
    
    @Test
    public void after2() {
        VectorClock<String> clock1 = new VectorClock<String>();
        clock1.add("roger");
        clock1.add("roger");
        
        VectorClock<String> clock2 = new VectorClock<String>();
        clock2.add("roger");
        
        TestCase.assertEquals(Occured.AFTER, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.BEFORE, clock2.compareTo(clock1));
    }
    
    @Test
    public void before() {
        VectorClock<String> clock1 = new VectorClock<String>();
        clock1.add("roger");
        
        VectorClock<String> clock2 = new VectorClock<String>();
        clock2.add("odvar");
        clock2.add("roger");
        
        TestCase.assertEquals(Occured.BEFORE, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.AFTER, clock2.compareTo(clock1));
    }
}
