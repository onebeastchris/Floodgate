/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.aes;

import java.security.Key;
import java.util.List;
import javax.crypto.KeyGenerator;
import org.geysermc.floodgate.core.crypto.KeyProducer;
import org.geysermc.floodgate.core.crypto.RandomUtils;

public final class AesKeyProducer implements KeyProducer {
    private static final int KEY_SIZE = 128;

    @Override
    public List<Key> produce() {
        try {
            var keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(KEY_SIZE, RandomUtils.secureRandom());
            return List.of(keyGenerator.generateKey());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
