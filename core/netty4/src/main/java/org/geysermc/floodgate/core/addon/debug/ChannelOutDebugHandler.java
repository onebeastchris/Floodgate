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
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.concurrent.atomic.AtomicInteger;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.util.Constants;

@Sharable
public final class ChannelOutDebugHandler extends MessageToByteEncoder<ByteBuf> {
    private final String direction;
    private final FloodgateLogger logger;

    private final AtomicInteger packetCount;

    public ChannelOutDebugHandler(
            String implementationType, boolean toServer, AtomicInteger packetCount, FloodgateLogger logger) {
        this.direction = implementationType + (toServer ? " -> Server" : " -> Player");
        this.logger = logger;
        this.packetCount = packetCount;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        try {
            int index = msg.readerIndex();

            if (packetCount.getAndIncrement() < Constants.MAX_DEBUG_PACKET_COUNT) {
                logger.info("{} {}:\n{}", direction, packetCount.get(), ByteBufUtil.prettyHexDump(msg));
            }

            // reset index
            msg.readerIndex(index);

            out.writeBytes(msg);
        } catch (Exception exception) {
            logger.error("Error in ChannelOutDebugHandler", exception);
        }
    }
}
