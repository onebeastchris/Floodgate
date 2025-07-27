/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.packethandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.geysermc.floodgate.api.util.TriFunction;
import org.geysermc.floodgate.core.packet.PacketHandlersImpl;

public class ChannelInPacketHandler extends SimpleChannelInboundHandler<Object> {
    private final PacketHandlersImpl packetHandlers;
    private final boolean toServer;

    public ChannelInPacketHandler(PacketHandlersImpl packetHandlers, boolean toServer) {
        this.packetHandlers = packetHandlers;
        this.toServer = toServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Object packet = msg;
        for (TriFunction<ChannelHandlerContext, Object, Boolean, Object> consumer :
                packetHandlers.getPacketHandlers(msg.getClass())) {

            Object res = consumer.apply(ctx, msg, toServer);
            if (!res.equals(msg)) {
                packet = res;
            }
        }

        ctx.fireChannelRead(packet);
    }
}
