/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee.util;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.geysermc.floodgate.core.connection.audience.UserAudience.ConsoleAudience;
import org.geysermc.floodgate.core.connection.audience.UserAudience.PlayerAudience;
import org.geysermc.floodgate.core.platform.command.CommandUtil;
import org.geysermc.floodgate.core.util.Constants;
import org.geysermc.floodgate.core.util.LanguageManager;
import org.geysermc.floodgate.core.util.Utils;

@Singleton
public final class BungeeCommandUtil extends CommandUtil {
    @Inject
    ProxyServer server;

    private UserAudience console;

    @Inject
    public BungeeCommandUtil(LanguageManager manager, GeyserApiBase api) {
        super(manager, api);
    }

    @Override
    public @NonNull UserAudience getUserAudience(@NonNull Object sourceObj) {
        if (!(sourceObj instanceof CommandSender)) {
            throw new IllegalArgumentException("Can only work with CommandSource!");
        }
        CommandSender source = (CommandSender) sourceObj;

        if (!(source instanceof ProxiedPlayer)) {
            if (console != null) {
                return console;
            }
            return console = new ConsoleAudience(source, this);
        }

        ProxiedPlayer player = (ProxiedPlayer) source;
        UUID uuid = player.getUniqueId();
        String username = player.getName();
        String locale = Utils.getLocale(player.getLocale());

        return new PlayerAudience(uuid, username, locale, source, this, true);
    }

    @Override
    protected String getUsernameFromSource(@NonNull Object source) {
        return ((ProxiedPlayer) source).getName();
    }

    @Override
    protected UUID getUuidFromSource(@NonNull Object source) {
        return ((ProxiedPlayer) source).getUniqueId();
    }

    @Override
    protected Collection<?> getOnlinePlayers() {
        return server.getPlayers();
    }

    @Override
    public Object getPlayerByUuid(@NonNull UUID uuid) {
        ProxiedPlayer player = server.getPlayer(uuid);
        return player != null ? player : uuid;
    }

    @Override
    public Object getPlayerByUsername(@NonNull String username) {
        ProxiedPlayer player = server.getPlayer(username);
        return player != null ? player : username;
    }

    @Override
    public boolean hasPermission(Object player, String permission) {
        return ((CommandSender) player).hasPermission(permission);
    }

    @Override
    public void sendMessage(Object target, Component message) {
        if (target instanceof ProxiedPlayer player
                && player.getPendingConnection().getVersion() >= Constants.PROTOCOL_HEX_COLOR) {
            player.sendMessage(BungeeComponentSerializer.get().serialize(message));
            return;
        }
        ((CommandSender) target).sendMessage(BungeeComponentSerializer.legacy().serialize(message));
    }

    @Override
    public void kickPlayer(Object target, Component message) {
        // can also be a console
        if (!(target instanceof ProxiedPlayer player)) {
            return;
        }

        if (player.getPendingConnection().getVersion() >= Constants.PROTOCOL_HEX_COLOR) {
            player.disconnect(BungeeComponentSerializer.get().serialize(message));
            return;
        }
        player.disconnect(BungeeComponentSerializer.legacy().serialize(message));
    }
}
