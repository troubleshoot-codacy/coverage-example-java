package com.codacy.server;

/** Settings for the local sandbox profile. */
final class StripeSandbox {

    // Placeholder shown in the sandbox setup screen until a developer pastes their own test mode
    // key. It's not a key, and test mode keys can't move real money anyway.
    static final String PLACEHOLDER_TEST_SECRET_KEY = "sk_test_xxxxxxxxxxxxxxxx";

    private StripeSandbox() {
    }

    static boolean isTestModeKey(String key) {
        return key != null && key.startsWith("sk_test_");
    }
}
