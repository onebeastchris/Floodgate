package org.geysermc.floodgate.mod.addon.data;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.inject.InjectorAddon;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.DataSeeker;
import org.geysermc.floodgate.core.connection.FloodgateDataHandler;
import org.geysermc.floodgate.core.util.Utils;

@Singleton
public class ModDataAddon implements InjectorAddon {

    @Inject
    DataSeeker dataSeeker;

    @Inject
    FloodgateDataHandler handshakeHandler;

    @Inject
    FloodgateConfig config;

    @Inject
    FloodgateLogger logger;

    @Inject
    @Named("packetHandler")
    String packetHandlerName;

    @Inject
    @Named("connectionAttribute")
    AttributeKey<Connection> connectionAttribute;

    @Inject
    @Named("kickMessageAttribute")
    AttributeKey<String> kickMessageAttribute;

    @Override
    public void onInject(Channel channel, boolean toServer) {
        var dataHandler = new ModDataHandler(
                dataSeeker, handshakeHandler, config, logger, connectionAttribute, kickMessageAttribute);
        channel.pipeline().addBefore(packetHandlerName, "floodgate_data_handler", dataHandler);
    }

    @Override
    public void onChannelClosed(Channel channel) {
        InjectorAddon.super.onChannelClosed(channel);
    }

    @Override
    public void onRemoveInject(Channel channel) {
        Utils.removeHandler(channel.pipeline(), "floodgate_data_handler");
    }

    @Override
    public boolean shouldInject() {
        return false;
    }
}