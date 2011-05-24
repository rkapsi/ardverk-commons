package org.ardverk.version;

import junit.framework.TestCase;

import org.junit.Test;

public class VectorClockTest {

    @Test
    public void identical1() {
        VectorClock<String> clock = VectorClock.create("roger");
        
        TestCase.assertEquals(Occured.IDENTICAL, clock.compareTo(clock));
    }
    
    @Test
    public void identical2() {
        VectorClock<String> clock1 = VectorClock.create("roger");
        VectorClock<String> clock2 = VectorClock.create("roger");
        
        TestCase.assertEquals(Occured.IDENTICAL, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.IDENTICAL, clock2.compareTo(clock1));
        
        clock1 = clock1.update("roger");
        clock2 = clock2.update("roger");
        
        TestCase.assertEquals(Occured.IDENTICAL, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.IDENTICAL, clock2.compareTo(clock1));
    }
    
    @Test
    public void identical3() {
        VectorClock<String> clock1 = VectorClock.create("roger").update("odvar");
        VectorClock<String> clock2 = VectorClock.create("odvar").update("roger");
        
        TestCase.assertEquals(Occured.IDENTICAL, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.IDENTICAL, clock2.compareTo(clock1));
    }
    
    @Test
    public void equal1() {
        VectorClock<String> clock1 = VectorClock.create("roger").update("odvar");
        VectorClock<String> clock2 = VectorClock.create("odvar").update("roger");
        
        TestCase.assertEquals(Occured.IDENTICAL, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.IDENTICAL, clock2.compareTo(clock1));
        
        TestCase.assertEquals(clock1, clock2);
        TestCase.assertEquals(clock1.hashCode(), clock2.hashCode());
    }
    
    @Test
    public void concurrently1() {
        VectorClock<String> clock1 = VectorClock.create("roger");
        
        VectorClock<String> clock2 = VectorClock.create("odvar");
        
        TestCase.assertEquals(Occured.CONCURRENTLY, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.CONCURRENTLY, clock2.compareTo(clock1));
    }
    
    @Test
    public void after1() {
        VectorClock<String> clock1 = VectorClock.create("roger").update("odvar");
        
        VectorClock<String> clock2 = VectorClock.create("odvar");
        
        TestCase.assertEquals(Occured.AFTER, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.BEFORE, clock2.compareTo(clock1));
    }
    
    @Test
    public void after2() {
        VectorClock<String> clock1 = VectorClock.create("roger").update("roger");
        
        VectorClock<String> clock2 = VectorClock.create("roger");
        
        TestCase.assertEquals(Occured.AFTER, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.BEFORE, clock2.compareTo(clock1));
    }
    
    @Test
    public void before() {
        VectorClock<String> clock1 = VectorClock.create("roger");
        
        VectorClock<String> clock2 = VectorClock.create("odvar").update("roger");
        
        TestCase.assertEquals(Occured.BEFORE, clock1.compareTo(clock2));
        TestCase.assertEquals(Occured.AFTER, clock2.compareTo(clock1));
    }
}
