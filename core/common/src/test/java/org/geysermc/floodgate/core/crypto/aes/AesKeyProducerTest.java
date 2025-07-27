/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.aes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Arrays;
import java.util.stream.Stream;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

final class AesKeyProducerTest {
    @Test
    void produceSingle() {
        var keys = new AesKeyProducer().produce();
        assertEquals(1, keys.size());
        var key = keys.get(0);
        assertInstanceOf(SecretKey.class, key);
        assertEquals("AES", key.getAlgorithm());
    }

    @Test
    void produceUnique() {
        var sampleSize = 5;

        var producer = new AesKeyProducer();
        var distinctKeys = Stream.generate(() -> producer.produce().get(0))
                .limit(sampleSize)
                .map(key -> Arrays.toString(key.getEncoded()))
                .distinct();
        assertEquals(sampleSize, distinctKeys.count());
    }
}
