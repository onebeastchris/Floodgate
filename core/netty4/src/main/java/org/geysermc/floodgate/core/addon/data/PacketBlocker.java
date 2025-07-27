/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.data;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * In Floodgate the PacketBlocker is used to temporarily prevent packets from being decoded. A
 * similar system is used to prevent packets from being handled while Floodgate is processing the
 * login. The old system blocked the thread which was processing the Floodgate login, but that
 * doesn't only block the packets for that specific user, it's shared between multiple users causing
 * them to lag or sometimes timeout.
 * <br>
 * The reason why we prevent packets from being handled is because keeping the packet order is
 * important. That is also the reason why we prevent packets from being decoded during that time.
 * The 'login start' packet for example can only be decoded after the handshake packet has been
 * handled, because the server can only successfully decode the packet after the handshake packet
 * caused the server to switch to the login state.
 */
public class PacketBlocker extends ChannelInboundHandlerAdapter {
    private final Queue<Object> packetQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean blockPackets;

    private ChannelHandlerContext ctx;

    public void enable() {
        blockPackets = true;
    }

    public void disable() {
        blockPackets = false;

        Object packet;
        while ((packet = packetQueue.poll()) != null) {
            ctx.fireChannelRead(packet);
        }
        ctx.pipeline().remove(this);
    }

    public boolean enabled() {
        return blockPackets;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (blockPackets || !packetQueue.isEmpty()) {
            packetQueue.add(msg);
            return;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
