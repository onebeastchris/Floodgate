/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.ed25519;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

final class Ed25519KeyCodecTest {
    private static final Ed25519KeyCodec codec = new Ed25519KeyCodec();

    @ParameterizedTest
    @ValueSource(
            strings = {
                "MC4CAQAwBQYDK2VwBCIEINFyuLU8O7U/w4nkC5RCzTb2BnlUy8kjo1jwCkluqgYZ",
                "MC4CAQAwBQYDK2VwBCIEII49NjdgnRL/EZsat0qsx1owAkEMj3rtLbNpjd9mbKSf"
            })
    void roundtripPrivate(String key) {
        var encodedKey = key.getBytes(StandardCharsets.UTF_8);
        var decoded = codec.decode(encodedKey, true);
        var encoded = codec.encode(decoded);
        assertArrayEquals(encodedKey, encoded);
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "MCowBQYDK2VwAyEA3lAOkEnih8ucexsKDtJb+eiwmMZgSLcCWNjSm+RCF9w=",
                "MCowBQYDK2VwAyEAepV2vnvTx68hraRTtF8jKK8POX/60i3jVMHc9BxEVkE="
            })
    void roundtripPublic(String key) {
        var encodedKey = key.getBytes(StandardCharsets.UTF_8);
        var decoded = codec.decode(encodedKey, false);
        var encoded = codec.encode(decoded);
        assertArrayEquals(encodedKey, encoded);
    }

    @ParameterizedTest
    @CsvSource({
        "MC4CAQAwBQYDK2VwBCIEINFyuLU8O7U/w4nkC5RCzTb2BnlUy8kjo1jwCkluqgYZ, MCowBQYDK2VwAyEA3lAOkEnih8ucexsKDtJb+eiwmMZgSLcCWNjSm+RCF9w=",
        "MC4CAQAwBQYDK2VwBCIEII49NjdgnRL/EZsat0qsx1owAkEMj3rtLbNpjd9mbKSf, MCowBQYDK2VwAyEAepV2vnvTx68hraRTtF8jKK8POX/60i3jVMHc9BxEVkE="
    })
    void decodeFromDirectory(ArgumentsAccessor encodedKeyPair, @TempDir Path tempDir) throws IOException {
        var privateKeyFileContent = encodedKeyPair.getString(0).getBytes(StandardCharsets.UTF_8);
        var publicKeyFileContent = encodedKeyPair.getString(1).getBytes(StandardCharsets.UTF_8);

        Files.write(tempDir.resolve("floodgate-private.key"), privateKeyFileContent);
        Files.write(tempDir.resolve("floodgate-public.der"), publicKeyFileContent);

        var decoded = codec.decode(tempDir);
        assertArrayEquals(privateKeyFileContent, codec.encode(decoded.getPrivate()));
        assertArrayEquals(publicKeyFileContent, codec.encode(decoded.getPublic()));
    }

    @ParameterizedTest
    @CsvSource({
        "MC4CAQAwBQYDK2VwBCIEINFyuLU8O7U/w4nkC5RCzTb2BnlUy8kjo1jwCkluqgYZ, MCowBQYDK2VwAyEA3lAOkEnih8ucexsKDtJb+eiwmMZgSLcCWNjSm+RCF9w=",
        "MC4CAQAwBQYDK2VwBCIEII49NjdgnRL/EZsat0qsx1owAkEMj3rtLbNpjd9mbKSf, MCowBQYDK2VwAyEAepV2vnvTx68hraRTtF8jKK8POX/60i3jVMHc9BxEVkE="
    })
    void encodeToDirectory(ArgumentsAccessor encodedKeyPair, @TempDir Path tempDir) throws IOException {
        var privateKeyFileContent = encodedKeyPair.getString(0).getBytes(StandardCharsets.UTF_8);
        var publicKeyFileContent = encodedKeyPair.getString(1).getBytes(StandardCharsets.UTF_8);

        var privateKey = (PrivateKey) codec.decode(privateKeyFileContent, true);
        var publicKey = (PublicKey) codec.decode(publicKeyFileContent, false);

        codec.encode(new KeyPair(publicKey, privateKey), tempDir);

        assertArrayEquals(privateKeyFileContent, Files.readAllBytes(tempDir.resolve("floodgate-private.key")));
        assertArrayEquals(publicKeyFileContent, Files.readAllBytes(tempDir.resolve("floodgate-public.der")));
    }
}
