/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection;

import java.net.InetAddress;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.checkerframework.common.value.qual.IntRange;
import org.geysermc.api.Geyser;
import org.geysermc.api.connection.Connection;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.floodgate.core.api.legacy.LegacyPlayerWrapper;
import org.geysermc.floodgate.core.api.legacy.PropertyGlue;
import org.geysermc.floodgate.util.BedrockData;
import org.geysermc.floodgate.util.LinkedPlayer;

public abstract class FloodgateConnection implements Connection {
    private final PropertyGlue propertyGlue = new PropertyGlue();
    private LegacyPlayerWrapper legacyPlayer;

    public abstract @NonNull UUID identity();

    public abstract @NonNull InetAddress ip();

    public abstract @Nullable LinkedPlayer linkedPlayer();

    @Override
    public boolean isLinked() {
        return linkedPlayer() != null;
    }

    @Override
    public boolean sendForm(@NonNull Form form) {
        return Geyser.api().sendForm(javaUuid(), form);
    }

    @Override
    public boolean sendForm(@NonNull FormBuilder<?, ?, ?> formBuilder) {
        return Geyser.api().sendForm(javaUuid(), formBuilder);
    }

    @Override
    public boolean transfer(@NonNull String address, @IntRange(from = 0L, to = 65535L) int port) {
        return Geyser.api().transfer(javaUuid(), address, port);
    }

    public abstract @This FloodgateConnection linkedPlayer(@Nullable LinkedPlayer linkedPlayer);

    public BedrockData toBedrockData() {
        return BedrockData.of(
                version(),
                bedrockUsername(),
                xuid(),
                platform().ordinal(),
                languageCode(),
                uiProfile().ordinal(),
                inputMode().ordinal(),
                ip().getHostAddress(),
                linkedPlayer(),
                false,
                0,
                null);
    }

    public LegacyPlayerWrapper legacySelf() {
        if (legacyPlayer == null) {
            legacyPlayer = new LegacyPlayerWrapper(this, javaUsername(), javaUuid());
        }
        return legacyPlayer;
    }

    public PropertyGlue propertyGlue() {
        return propertyGlue;
    }
}
