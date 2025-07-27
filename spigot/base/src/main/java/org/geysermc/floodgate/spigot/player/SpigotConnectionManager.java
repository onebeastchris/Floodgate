/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.player;

import jakarta.inject.Singleton;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.floodgate.core.connection.ConnectionManager;

@Singleton
public class SpigotConnectionManager extends ConnectionManager {
    @Override
    protected @Nullable Object platformIdentifierOrConnectionFor(Object input) {
        // in PlayerList#canPlayerLogin the old players with the same profile are disconnected (
        // PlayerKickEvent, see ServerGamePacketListener#disconnect &
        // PlayerQuitEvent, see PlayerList#remove
        // ) before the first event runs with the Player instance (PlayerLoginEvent).
        // This means that we can always use the Player's uuid
        if (input instanceof Player player) {
            return connectionByUuid(player.getUniqueId());
        }
        return null;
    }
}
