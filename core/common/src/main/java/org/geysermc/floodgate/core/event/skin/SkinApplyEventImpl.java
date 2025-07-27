/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.event.skin;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.api.connection.Connection;
import org.geysermc.event.util.AbstractCancellable;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.core.connection.FloodgateConnection;

public class SkinApplyEventImpl extends AbstractCancellable implements SkinApplyEvent {
    private final FloodgatePlayer player;
    private final SkinData currentSkin;
    private SkinData newSkin;

    public SkinApplyEventImpl(
            @NonNull Connection connection, @Nullable SkinData currentSkin, @NonNull SkinData newSkin) {
        Objects.requireNonNull(connection);
        this.player = ((FloodgateConnection) connection).legacySelf();
        this.currentSkin = currentSkin;
        this.newSkin = Objects.requireNonNull(newSkin);
    }

    @Override
    public @NonNull FloodgatePlayer player() {
        return player;
    }

    public @Nullable SkinData currentSkin() {
        return currentSkin;
    }

    public @NonNull SkinData newSkin() {
        return newSkin;
    }

    public SkinApplyEventImpl newSkin(@NonNull SkinData skinData) {
        this.newSkin = Objects.requireNonNull(skinData);
        return this;
    }
}
