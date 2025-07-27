/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.packet;

/**
 * For advanced users only! You shouldn't play with this unless you know what you're doing.
 */
public interface PacketHandler<C> {
    /**
     * Called when a registered packet has been seen.
     *
     * @param ctx         the channel handler context of the connection
     * @param packet      the packet instance
     * @param serverbound if the packet is serverbound
     * @return the packet it should forward. Can be null or a different packet / instance
     */
    Object handle(C ctx, Object packet, boolean serverbound);
}
