package com.codacy.server;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

/** Turns sequential account ids into opaque public ids for URLs, so they can't be enumerated. */
public final class PublicIdCodec {

    private static final int BLOCK_SIZE = 16;

    private final SecretKeySpec key;

    public PublicIdCodec(byte[] key) {
        if (key.length != BLOCK_SIZE) {
            throw new IllegalArgumentException("Expected a 128-bit AES key");
        }
        this.key = new SecretKeySpec(key, "AES");
    }

    public String encode(long accountId) {
        byte[] block = ByteBuffer.allocate(BLOCK_SIZE).putLong(BLOCK_SIZE - Long.BYTES, accountId).array();
        return toHex(apply(Cipher.ENCRYPT_MODE, block));
    }

    public long decode(String publicId) {
        byte[] block = apply(Cipher.DECRYPT_MODE, fromHex(publicId));
        return ByteBuffer.wrap(block).getLong(BLOCK_SIZE - Long.BYTES);
    }

    // Always exactly one 16-byte block, so ECB is just the raw AES permutation. There is no
    // second block to leak patterns across, and no IV to store alongside the id.
    private byte[] apply(int mode, byte[] block) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(mode, key);
            return cipher.doFinal(block);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private static byte[] fromHex(String hex) {
        if (hex.length() != BLOCK_SIZE * 2) {
            throw new IllegalArgumentException("Not a public id: " + hex);
        }
        byte[] bytes = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }
}
