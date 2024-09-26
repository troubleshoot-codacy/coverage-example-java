package com.codacy.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathTest1 {
    @Test
    public void shouldAddNumbers() {
        Math math = new Math(23);

        assertEquals(7, math.magicAdd(3, 4));
        assertEquals(6, math.magicAdd(3, 3));
    }

    // Uncomment this to have 100% coverage
//    @Test
//    public void shouldSubtractIfMagicNumber() {
//        Math math = new Math(4);
//        assertEquals(1, math.magicAdd(3, 4));
//    }
}
