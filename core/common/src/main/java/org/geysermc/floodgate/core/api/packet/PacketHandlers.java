/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.packet;

import org.geysermc.floodgate.api.util.TriFunction;

public interface PacketHandlers<C> {
    /**
     * Register a specific class for a specific consumer.
     *
     * @param handler     the packet handler instance
     * @param packetClass the class to start listening for
     * @param consumer    the consumer to call once the packet has been seen
     */
    void register(PacketHandler<C> handler, Class<?> packetClass, TriFunction<C, Object, Boolean, Object> consumer);

    /**
     * Register a specific class for the given packet handler's {@link
     * PacketHandler#handle(Object, Object, boolean)}.
     *
     * @param handler     the packet handler instance
     * @param packetClass the class to start listening for
     */
    default void register(PacketHandler<C> handler, Class<?> packetClass) {
        register(handler, packetClass, handler::handle);
    }

    /**
     * Register every packet for the given packet handler's {@link PacketHandler#handle(Object, Object, boolean)}
     */
    void registerAll(PacketHandler<C> handler);

    /**
     * Unregisters all handlers registered under the given packet handler
     *
     * @param handler the packet handler instance
     */
    void deregister(PacketHandler<C> handler);
}
