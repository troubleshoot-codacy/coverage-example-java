package com.codacy.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SomeFileTest {
    @Test
    public void shouldPing() {
        SomeFile file = new SomeFile();

        assertEquals("ping", file.ping());
    }
}
