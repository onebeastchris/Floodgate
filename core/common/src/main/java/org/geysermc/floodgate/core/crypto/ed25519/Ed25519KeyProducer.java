/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.ed25519;

import java.security.Key;
import java.security.KeyPairGenerator;
import java.util.List;
import org.geysermc.floodgate.core.crypto.KeyProducer;

public final class Ed25519KeyProducer implements KeyProducer {
    @Override
    public List<Key> produce() {
        try {
            var keyGenerator = KeyPairGenerator.getInstance("Ed25519");
            var keyPair = keyGenerator.generateKeyPair();
            return List.of(keyPair.getPrivate(), keyPair.getPublic());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
