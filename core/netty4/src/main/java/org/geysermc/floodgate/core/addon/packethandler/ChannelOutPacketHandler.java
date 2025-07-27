/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.packethandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.geysermc.floodgate.api.util.TriFunction;
import org.geysermc.floodgate.core.packet.PacketHandlersImpl;

public class ChannelOutPacketHandler extends MessageToMessageEncoder<Object> {
    private final PacketHandlersImpl packetHandlers;
    private final boolean toServer;

    public ChannelOutPacketHandler(PacketHandlersImpl packetHandlers, boolean toServer) {
        this.packetHandlers = packetHandlers;
        this.toServer = toServer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) {
        Object packet = msg;
        for (TriFunction<ChannelHandlerContext, Object, Boolean, Object> consumer :
                packetHandlers.getPacketHandlers(msg.getClass())) {

            Object res = consumer.apply(ctx, msg, toServer);
            if (!res.equals(msg)) {
                packet = res;
            }
        }

        out.add(packet);
    }
}
