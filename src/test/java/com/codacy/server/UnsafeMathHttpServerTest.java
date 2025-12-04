package com.codacy.server;


import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Minimal test for teaching purposes. Does not actually test UnsafeMathHttpServer functionality.
 */
public class UnsafeMathHttpServerTest {

    @Test
    public void testToDo() {
        assertTrue(0 == 0);
    }

    @Test
    public void testServerClassExists() {
        // This test only checks that the class can be loaded
        UnsafeMathHttpServer server = null;
        assertTrue(server == null);
    }
}
