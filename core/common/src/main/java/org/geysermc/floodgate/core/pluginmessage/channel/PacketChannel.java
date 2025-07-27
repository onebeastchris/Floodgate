/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.pluginmessage.channel;

import jakarta.inject.Inject;
import java.util.UUID;
import org.geysermc.floodgate.core.api.UnsafeFloodgateApi;
import org.geysermc.floodgate.core.platform.pluginmessage.PluginMessageUtils;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel;

public final class PacketChannel implements PluginMessageChannel {
    @Inject
    PluginMessageUtils pluginMessageUtils;

    @Override
    public String getIdentifier() {
        return "floodgate:packet";
    }

    @Override
    public Result handleProxyCall(byte[] data, UUID sourceUuid, String sourceUsername, Identity sourceIdentity) {
        if (sourceIdentity == Identity.SERVER) {
            // send it to the client
            return Result.forward();
        }

        if (sourceIdentity == Identity.PLAYER) {
            return handleServerCall(data, sourceUuid, sourceUsername);
        }

        return Result.handled();
    }

    @Override
    public Result handleServerCall(byte[] data, UUID playerUuid, String playerUsername) {
        return Result.kick("Cannot send packets from Geyser/Floodgate to Floodgate");
    }

    public boolean sendPacket(UUID player, byte[] packet, UnsafeFloodgateApi api) {
        if (api == null) {
            throw new IllegalArgumentException("Can only send a packet using the unsafe api");
        }
        return pluginMessageUtils.sendMessage(player, getIdentifier(), packet);
    }
}
