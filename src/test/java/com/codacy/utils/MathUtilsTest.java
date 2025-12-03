package com.codacy.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilsTest {
    @Test
    public void shouldAddNumbers() {
        Math math = new Math(23);

        assertEquals(7, math.magicAdd(3, 4));
    }

    @Test
    public void shouldSubtractNumbers() {
        Math math = new Math(5);

        assertEquals(1, math.magicAdd(4, 3));
    }

}
