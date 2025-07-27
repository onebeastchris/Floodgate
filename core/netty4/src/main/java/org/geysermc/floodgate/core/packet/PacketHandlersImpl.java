/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.packet;

import io.netty.channel.ChannelHandlerContext;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geysermc.floodgate.api.util.TriFunction;
import org.geysermc.floodgate.core.api.packet.PacketHandler;
import org.geysermc.floodgate.core.api.packet.PacketHandlers;

@Singleton
public final class PacketHandlersImpl implements PacketHandlers<ChannelHandlerContext> {
    private final Map<PacketHandler<ChannelHandlerContext>, List<HandlerEntry>> handlers = new HashMap<>();
    private final Set<TriFunction<ChannelHandlerContext, Object, Boolean, Object>> globalPacketHandlers =
            new HashSet<>();
    private final Map<Class<?>, Set<TriFunction<ChannelHandlerContext, Object, Boolean, Object>>> packetHandlers =
            new HashMap<>();

    @Override
    public void register(
            PacketHandler<ChannelHandlerContext> handler,
            Class<?> packetClass,
            TriFunction<ChannelHandlerContext, Object, Boolean, Object> consumer) {

        if (handler == null || packetClass == null || consumer == null) {
            return;
        }

        handlers.computeIfAbsent(handler, $ -> new ArrayList<>()).add(new HandlerEntry(packetClass, consumer));

        packetHandlers
                .computeIfAbsent(packetClass, $ -> new HashSet<>(globalPacketHandlers))
                .add(consumer);
    }

    @Override
    public void registerAll(PacketHandler<ChannelHandlerContext> handler) {
        if (handler == null) {
            return;
        }

        TriFunction<ChannelHandlerContext, Object, Boolean, Object> packetHandler = handler::handle;

        handlers.computeIfAbsent(handler, $ -> new ArrayList<>()).add(new HandlerEntry(null, packetHandler));

        globalPacketHandlers.add(packetHandler);
        for (Set<TriFunction<ChannelHandlerContext, Object, Boolean, Object>> handle : packetHandlers.values()) {
            handle.add(packetHandler);
        }
    }

    @Override
    public void deregister(PacketHandler<ChannelHandlerContext> handler) {
        if (handler == null) {
            return;
        }

        List<HandlerEntry> values = handlers.remove(handler);
        if (values != null) {
            for (HandlerEntry value : values) {
                Set<?> handlers = packetHandlers.get(value.packet());

                if (handlers != null) {
                    handlers.removeIf(o -> o.equals(value.handler()));
                    if (handlers.isEmpty()) {
                        packetHandlers.remove(value.packet());
                    }
                }

                globalPacketHandlers.remove(value.handler());
            }
        }
    }

    public Collection<TriFunction<ChannelHandlerContext, Object, Boolean, Object>> getPacketHandlers(Class<?> packet) {
        return packetHandlers.getOrDefault(packet, Collections.emptySet());
    }

    public boolean hasHandlers() {
        return !handlers.isEmpty();
    }

    private record HandlerEntry(Class<?> packet, TriFunction<ChannelHandlerContext, Object, Boolean, Object> handler) {}
}
