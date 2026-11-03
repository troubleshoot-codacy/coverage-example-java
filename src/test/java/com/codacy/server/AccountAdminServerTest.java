package com.codacy.server;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AccountAdminServerTest {
    @Test
    public void shouldEncryptToken() {
        assertNotEquals("token", AccountAdminServer.encrypt("token"));
    }

    @Test
    public void shouldRecognizeAdmins() {
        assertTrue(AccountAdminServer.isAdmin("aadmin"));
        assertFalse(AccountAdminServer.isAdmin("user"));
    }
}
