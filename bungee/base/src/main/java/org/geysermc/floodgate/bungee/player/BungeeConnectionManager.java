/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee.player;

import static java.util.Objects.requireNonNull;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.lang.reflect.Field;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.core.connection.ConnectionManager;
import org.geysermc.floodgate.core.util.ReflectionUtils;

@Singleton
public final class BungeeConnectionManager extends ConnectionManager {
    private static final Field CHANNEL_WRAPPER;

    @Inject
    @Named("connectionAttribute")
    AttributeKey<Connection> connectionAttribute;

    @Override
    protected @Nullable Object platformIdentifierOrConnectionFor(Object input) {
        if (input instanceof ProxiedPlayer player) {
            return connectionByPlatformIdentifier(player.getPendingConnection());
        }
        if (input instanceof PendingConnection pendingConnection) {
            return channelFor(pendingConnection);
        }
        if (input instanceof Channel channel) {
            return channel.attr(connectionAttribute).get();
        }
        return null;
    }

    public Channel channelFor(PendingConnection connection) {
        ChannelWrapper wrapper = ReflectionUtils.getCastedValue(connection, CHANNEL_WRAPPER);
        return wrapper.getHandle();
    }

    static {
        CHANNEL_WRAPPER = ReflectionUtils.getFieldOfType(InitialHandler.class, ChannelWrapper.class);
        requireNonNull(CHANNEL_WRAPPER, "ChannelWrapper field cannot be null");
    }
}
