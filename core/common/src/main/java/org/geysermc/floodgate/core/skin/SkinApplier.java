/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.skin;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;

public interface SkinApplier {
    /**
     * Apply a skin to a {@link Connection player}
     *
     * @param connection player to apply skin to
     * @param skinData data for skin to apply to player
     */
    void applySkin(@NonNull Connection connection, @NonNull SkinData skinData);
}
