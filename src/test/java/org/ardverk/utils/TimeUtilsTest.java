package org.ardverk.utils;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

public class TimeUtilsTest {

    @Test
    public void convert() {
        long time = TimeUtils.convert(5L, TimeUnit.SECONDS, TimeUnit.MILLISECONDS);
        TestCase.assertEquals(5000L, time);
    }
}
