package com.codacy.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FooTest {
    @Test
    public void shouldAddNumbers() {
        Foo foo = new Foo();

        assertEquals("baar", foo.bar());
    }
}
