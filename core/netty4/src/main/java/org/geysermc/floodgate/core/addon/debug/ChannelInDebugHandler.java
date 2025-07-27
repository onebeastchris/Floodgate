/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.debug;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.atomic.AtomicInteger;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.util.Constants;

@Sharable
public final class ChannelInDebugHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final String direction;
    private final FloodgateLogger logger;

    private final AtomicInteger packetCount;

    public ChannelInDebugHandler(
            String implementationType, boolean toServer, AtomicInteger packetCount, FloodgateLogger logger) {
        this.direction = (toServer ? "Server -> " : "Player -> ") + implementationType;
        this.logger = logger;
        this.packetCount = packetCount;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        try {
            int index = msg.readerIndex();

            if (packetCount.getAndIncrement() < Constants.MAX_DEBUG_PACKET_COUNT) {
                logger.info("{} {}:\n{}", direction, packetCount.get(), ByteBufUtil.prettyHexDump(msg));
            }

            // reset index
            msg.readerIndex(index);

            ctx.fireChannelRead(msg.retain());
        } catch (Exception exception) {
            logger.error("Error in ChannelInDebugHandler", exception);
        }
    }
}
