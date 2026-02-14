package com.codacy.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MathTest {
    @Test
    public void shouldAddNumbers() {
        Math math = new Math(23);

        assertEquals(7, math.magicAdd(3, 4));
    }

    @Test
    public void testDummySecret() {
        String dummyPassword = "password123";
        System.out.println("Dummy password is: " + dummyPassword);
        assertTrue(dummyPassword.length() > 0);
    }

    // Uncomment this to have 100% coverage
//    @Test
//    public void shouldSubtractIfMagicNumber() {
//        Math math = new Math(4);
//        assertEquals(1, math.magicAdd(3, 4));
//    }
}