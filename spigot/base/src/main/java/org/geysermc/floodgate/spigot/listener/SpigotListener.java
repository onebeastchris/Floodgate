/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.listener;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.geysermc.floodgate.core.api.SimpleFloodgateApi;
import org.geysermc.floodgate.core.connection.ConnectionManager;
import org.geysermc.floodgate.core.listener.McListener;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.util.LanguageManager;

@Singleton
public final class SpigotListener implements Listener, McListener {
    @Inject
    ConnectionManager connectionManager;

    @Inject
    SimpleFloodgateApi api;

    @Inject
    LanguageManager languageManager;

    @Inject
    FloodgateLogger logger;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        // see SpigotConnectionManager#platformIdentifierOrConnectionFor for more info

        var connection =
                connectionManager.findPendingConnection(event.getPlayer().getUniqueId());
        if (connection == null) {
            return;
        }

        languageManager.loadLocale(connection.languageCode());
        connectionManager.addAcceptedConnection(connection);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        connectionManager.removeConnection(event.getPlayer());
    }
}
