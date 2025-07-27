/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity.pluginmessage;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent.ForwardResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import io.micronaut.context.BeanProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.geysermc.floodgate.core.listener.McListener;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.platform.pluginmessage.PluginMessageUtils;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel.Identity;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel.Result;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageManager;

@Singleton
public class VelocityPluginMessageUtils extends PluginMessageUtils implements McListener {
    @Inject
    BeanProvider<PluginMessageManager> pluginMessageManager;

    @Inject
    ProxyServer proxy;

    @Inject
    FloodgateLogger logger;

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        String channelId = event.getIdentifier().getId();
        PluginMessageChannel channel = pluginMessageManager.get().getChannel(channelId);
        if (channel == null) {
            return;
        }

        UUID sourceUuid = null;
        String sourceUsername = null;
        Identity sourceIdentity = Identity.UNKNOWN;

        ChannelMessageSource source = event.getSource();
        if (source instanceof Player) {
            Player player = (Player) source;
            sourceUuid = player.getUniqueId();
            sourceUsername = player.getUsername();
            sourceIdentity = Identity.PLAYER;

        } else if (source instanceof ServerConnection) {
            sourceIdentity = Identity.SERVER;
        }

        Result result = channel.handleProxyCall(event.getData(), sourceUuid, sourceUsername, sourceIdentity);

        event.setResult(result.isAllowed() ? ForwardResult.forward() : ForwardResult.handled());

        if (!result.isAllowed() && result.getReason() != null) {
            logKick(source, result.getReason());
        }
    }

    private void logKick(ChannelMessageSource source, String reason) {
        logger.error(reason + " Closing connection");
        ((Player) source).disconnect(Component.text(reason));
    }

    public boolean sendMessage(UUID player, boolean toServer, ChannelIdentifier identifier, byte[] data) {

        if (toServer) {
            return proxy.getPlayer(player)
                    .flatMap(Player::getCurrentServer)
                    .map(server -> server.sendPluginMessage(identifier, data))
                    .orElse(false);
        }

        return proxy.getPlayer(player)
                .map(value -> value.sendPluginMessage(identifier, data))
                .orElse(false);
    }

    @Override
    public boolean sendMessage(UUID player, boolean toServer, String channel, byte[] data) {
        return sendMessage(player, toServer, MinecraftChannelIdentifier.from(channel), data);
    }
}
