/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee.pluginmessage;

import io.micronaut.context.BeanProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.UUID;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.geysermc.floodgate.core.listener.McListener;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.platform.pluginmessage.PluginMessageUtils;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel.Identity;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel.Result;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageManager;

@Singleton
public final class BungeePluginMessageUtils extends PluginMessageUtils implements Listener, McListener {
    @Inject
    BeanProvider<PluginMessageManager> pluginMessageManager;

    @Inject
    FloodgateLogger logger;

    @EventHandler(priority = EventPriority.LOW)
    public void onPluginMessage(PluginMessageEvent event) {
        PluginMessageChannel channel = pluginMessageManager.get().getChannel(event.getTag());
        if (channel == null) {
            return;
        }

        UUID sourceUuid = null;
        String sourceUsername = null;
        Identity sourceIdentity = Identity.UNKNOWN;

        Connection source = event.getSender();
        if (source instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) source;
            sourceUuid = player.getUniqueId();
            sourceUsername = player.getName();
            sourceIdentity = Identity.PLAYER;

        } else if (source instanceof ServerConnection) {
            sourceIdentity = Identity.SERVER;
        }

        Result result = channel.handleProxyCall(event.getData(), sourceUuid, sourceUsername, sourceIdentity);

        event.setCancelled(!result.isAllowed());

        if (!result.isAllowed() && result.getReason() != null) {
            logKick(source, result.getReason());
        }
    }

    private void logKick(Connection source, String reason) {
        logger.error(reason + " Closing connection");
        source.disconnect(new TextComponent(reason));
    }

    @Override
    public boolean sendMessage(UUID player, boolean toServer, String channel, byte[] data) {
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(player);
        if (proxiedPlayer == null) {
            return false;
        }

        if (toServer) {
            proxiedPlayer.getServer().sendData(channel, data);
        } else {
            proxiedPlayer.sendData(channel, data);
        }
        return true;
    }
}
