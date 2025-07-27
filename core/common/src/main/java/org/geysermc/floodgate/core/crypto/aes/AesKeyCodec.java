/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.aes;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.geysermc.floodgate.core.crypto.KeyCodecSingle;

public final class AesKeyCodec implements KeyCodecSingle {
    @Override
    public SecretKey decode(byte[] keyBytes) {
        // we only started encoding keys with base64 since 3.0
        // we ignore if it's not valid base64 to allow those old keys to keep working
        try {
            keyBytes = Base64.getDecoder().decode(keyBytes);
        } catch (IllegalArgumentException ignored) {
        }

        return new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public byte[] encode(SecretKey key) {
        return Base64.getEncoder().encode(key.getEncoded());
    }
}
