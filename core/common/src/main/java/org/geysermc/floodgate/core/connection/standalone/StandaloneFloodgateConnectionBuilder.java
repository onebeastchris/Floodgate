/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.standalone;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.geysermc.api.util.BedrockPlatform;
import org.geysermc.api.util.InputMode;
import org.geysermc.api.util.UiProfile;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.FloodgateConnection;
import org.geysermc.floodgate.core.util.Utils;
import org.geysermc.floodgate.util.LinkedPlayer;

public class StandaloneFloodgateConnectionBuilder {
    private final FloodgateConfig config;
    private String version;
    private String username;
    private UUID identity;
    private String xuid;
    private BedrockPlatform deviceOs;
    private String languageCode;
    private UiProfile uiProfile;
    private InputMode inputMode;
    private InetAddress ip;
    private LinkedPlayer linkedPlayer;

    public StandaloneFloodgateConnectionBuilder(FloodgateConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    public @This StandaloneFloodgateConnectionBuilder version(String version) {
        this.version = Objects.requireNonNull(version);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder username(String username) {
        this.username = Objects.requireNonNull(username);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder identity(UUID identity) {
        this.identity = Objects.requireNonNull(identity);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder xuid(String xuid) {
        this.xuid = Objects.requireNonNull(xuid);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder deviceOs(BedrockPlatform deviceOs) {
        this.deviceOs = Objects.requireNonNull(deviceOs);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder languageCode(String languageCode) {
        this.languageCode = Objects.requireNonNull(languageCode);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder uiProfile(UiProfile uiProfile) {
        this.uiProfile = Objects.requireNonNull(uiProfile);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder inputMode(InputMode inputMode) {
        this.inputMode = Objects.requireNonNull(inputMode);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder ip(InetAddress ip) {
        this.ip = Objects.requireNonNull(ip);
        return this;
    }

    public @This StandaloneFloodgateConnectionBuilder linkedPlayer(@Nullable LinkedPlayer linkedPlayer) {
        this.linkedPlayer = linkedPlayer;
        return this;
    }

    public FloodgateConnection build() {
        // todo add an option to use identity instead of xuid
        UUID javaUniqueId = Utils.toFloodgateUniqueId(xuid);

        return new StandaloneFloodgateConnection(
                version,
                username,
                identity,
                xuid,
                javaUsername(),
                javaUniqueId,
                deviceOs,
                languageCode,
                uiProfile,
                inputMode,
                ip,
                linkedPlayer);
    }

    private String javaUsername() {
        String prefix = config.usernamePrefix();
        int usernameLength = Math.min(username.length(), 16 - prefix.length());
        String javaUsername = prefix + username.substring(0, usernameLength);
        if (config.replaceSpaces()) {
            javaUsername = javaUsername.replace(" ", "_");
        }
        return javaUsername;
    }
}
