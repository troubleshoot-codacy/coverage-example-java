package com.codacy.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilsTest {
    @Test
    public void shouldAddNumbers() {
        MathUtils math = new MathUtils(23);

        assertEquals(7, math.magicAdd(3, 4));
    }

}
