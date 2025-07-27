/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.api.unsafe;

import java.util.UUID;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public interface Unsafe {
    /**
     * Send a raw Bedrock packet to the given online Bedrock player.
     *
     * @param bedrockPlayer the uuid of the online Bedrock player
     * @param packetId      the id of the packet to send
     * @param packetData    the raw packet data
     */
    void sendPacket(UUID bedrockPlayer, int packetId, byte[] packetData);

    /**
     * Send a raw Bedrock packet to the given online Bedrock player.
     *
     * @param player     the Bedrock player to send the packet to
     * @param packetId   the id of the packet to send
     * @param packetData the raw packet data
     */
    default void sendPacket(FloodgatePlayer player, int packetId, byte[] packetData) {
        sendPacket(player.getCorrectUniqueId(), packetId, packetData);
    }
}
