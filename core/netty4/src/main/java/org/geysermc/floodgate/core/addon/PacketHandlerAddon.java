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
import org.geysermc.floodgate.core.Netty4;
import org.geysermc.floodgate.core.addon.packethandler.ChannelInPacketHandler;
import org.geysermc.floodgate.core.addon.packethandler.ChannelOutPacketHandler;
import org.geysermc.floodgate.core.api.inject.InjectorAddon;
import org.geysermc.floodgate.core.packet.PacketHandlersImpl;

@Singleton
public class PacketHandlerAddon implements InjectorAddon<Channel> {
    @Inject
    PacketHandlersImpl packetHandlers;

    @Inject
    @Named("packetEncoder")
    String packetEncoder;

    @Inject
    @Named("packetDecoder")
    String packetDecoder;

    @Override
    public void onInject(Channel channel, boolean toServer) {
        channel.pipeline()
                .addAfter(packetEncoder, "floodgate_phaddon_out", new ChannelOutPacketHandler(packetHandlers, toServer))
                .addAfter(packetDecoder, "floodgate_phaddon_in", new ChannelInPacketHandler(packetHandlers, toServer));
    }

    @Override
    public void onRemoveInject(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        Netty4.removeHandler(pipeline, "floodgate_phaddon_out");
        Netty4.removeHandler(pipeline, "floodgate_phaddon_in");
    }

    @Override
    public boolean shouldInject() {
        // this means that there has to be at least one PacketHandler registered to inject
        return packetHandlers.hasHandlers();
    }
}
