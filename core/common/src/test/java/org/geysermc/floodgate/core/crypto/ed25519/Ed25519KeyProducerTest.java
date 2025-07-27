/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.ed25519;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

final class Ed25519KeyProducerTest {
    @Test
    void produceSingle() {
        var keys = new Ed25519KeyProducer().produce();
        assertEquals(2, keys.size());

        var key = keys.get(0);
        assertInstanceOf(PrivateKey.class, key);
        assertEquals("EdDSA", key.getAlgorithm());

        key = keys.get(1);
        assertInstanceOf(PublicKey.class, key);
        assertEquals("EdDSA", key.getAlgorithm());
    }

    @Test
    void produceUnique() {
        var sampleSize = 5;

        var producer = new Ed25519KeyProducer();
        // ignore the public key
        var distinctKeys = Stream.generate(() -> producer.produce().get(0))
                .limit(sampleSize)
                .map(key -> Arrays.toString(key.getEncoded()))
                .distinct();
        assertEquals(sampleSize, distinctKeys.count());
    }
}
