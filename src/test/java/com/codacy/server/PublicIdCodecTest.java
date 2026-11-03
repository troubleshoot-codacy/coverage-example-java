package com.codacy.server;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PublicIdCodecTest {
    private final PublicIdCodec codec = new PublicIdCodec(new byte[16]);

    @Test
    public void shouldRoundTripAccountIds() {
        assertEquals(42L, codec.decode(codec.encode(42L)));
        assertNotEquals(codec.encode(42L), codec.encode(43L));
    }
}
