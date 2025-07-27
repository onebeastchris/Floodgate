/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.api.event.skin;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.geysermc.event.Cancellable;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public interface SkinApplyEvent extends Cancellable {
    /**
     * Returns the player that will receive the skin.
     */
    @NonNull FloodgatePlayer player();

    /**
     * Returns the skin texture currently applied to the player.
     */
    @Nullable SkinData currentSkin();

    /**
     * Returns the skin texture to be applied to the player.
     */
    @NonNull SkinData newSkin();

    /**
     * Sets the skin texture to be applied to the player
     *
     * @param skinData the skin to apply
     * @return this
     */
    @This SkinApplyEvent newSkin(@NonNull SkinData skinData);

    interface SkinData {
        /**
         * Returns the value of the skin texture.
         */
        @NonNull String value();

        /**
         * Returns the signature of the skin texture.
         */
        @NonNull String signature();
    }
}
