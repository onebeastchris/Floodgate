/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.pluginmessage.channel;

import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.geysermc.floodgate.core.platform.pluginmessage.PluginMessageUtils;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel;

public class TransferChannel implements PluginMessageChannel {
    @Inject
    PluginMessageUtils pluginMessageUtils;

    @Override
    public String getIdentifier() {
        return "floodgate:transfer";
    }

    @Override
    public Result handleProxyCall(byte[] data, UUID sourceUuid, String sourceUsername, Identity sourceIdentity) {
        if (sourceIdentity == Identity.SERVER) {
            // send it to the client
            return Result.forward();
        }

        if (sourceIdentity == Identity.PLAYER) {
            handleServerCall(data, sourceUuid, sourceUsername);
        }

        return Result.handled();
    }

    @Override
    public Result handleServerCall(byte[] data, UUID playerUuid, String playerUsername) {
        return Result.kick("I'm sorry, I'm unable to transfer a server :(");
    }

    public boolean sendTransfer(UUID player, String address, int port) {
        byte[] addressBytes = address.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[addressBytes.length + 4];

        data[0] = (byte) (port >> 24);
        data[1] = (byte) (port >> 16);
        data[2] = (byte) (port >> 8);
        data[3] = (byte) (port);
        System.arraycopy(addressBytes, 0, data, 4, addressBytes.length);

        return pluginMessageUtils.sendMessage(player, getIdentifier(), data);
    }
}
