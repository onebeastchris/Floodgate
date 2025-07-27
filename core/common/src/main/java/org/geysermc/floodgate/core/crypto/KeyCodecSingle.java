/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.crypto.SecretKey;

public interface KeyCodecSingle extends KeyCodec<SecretKey> {
    default SecretKey decode(Path keyDirectory) throws IOException {
        return decode(Files.readAllBytes(keyDirectory.resolve("floodgate.key")));
    }

    default void encode(SecretKey key, Path keyDirectory) throws IOException {
        Files.write(keyDirectory.resolve("floodgate.key"), encode(key));
    }

    SecretKey decode(byte[] keyBytes);

    byte[] encode(SecretKey key);
}
