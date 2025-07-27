/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;
import org.geysermc.floodgate.core.Netty4;
import org.geysermc.floodgate.core.addon.debug.ChannelInDebugHandler;
import org.geysermc.floodgate.core.addon.debug.ChannelOutDebugHandler;
import org.geysermc.floodgate.core.api.inject.InjectorAddon;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.logger.FloodgateLogger;

@Singleton
public final class DebugAddon implements InjectorAddon<Channel> {
    @Inject
    FloodgateConfig config;

    @Inject
    FloodgateLogger logger;

    @Inject
    @Named("implementationName")
    String implementationName;

    @Inject
    @Named("packetEncoder")
    String packetEncoder;

    @Inject
    @Named("packetDecoder")
    String packetDecoder;

    @Override
    public void onInject(Channel channel, boolean toServer) {
        logger.info("Successfully called onInject. To server? {} ({})", toServer, channel.id());

        var packetCount = new AtomicInteger();

        channel.pipeline()
                .addBefore(
                        packetEncoder,
                        "floodgate_debug_out",
                        new ChannelOutDebugHandler(implementationName, toServer, packetCount, logger))
                .addBefore(
                        packetDecoder,
                        "floodgate_debug_in",
                        new ChannelInDebugHandler(implementationName, toServer, packetCount, logger));
    }

    @Override
    public void onRemoveInject(Channel channel) {
        logger.info("Removing injection ({})", channel.id());
        ChannelPipeline pipeline = channel.pipeline();

        Netty4.removeHandler(pipeline, "floodgate_debug_out");
        Netty4.removeHandler(pipeline, "floodgate_debug_in");
    }

    @Override
    public boolean shouldInject() {
        return config.debug();
    }
}
