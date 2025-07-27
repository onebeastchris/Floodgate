/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.pluginmessage;

import java.util.UUID;

public class PluginMessageUtils {
    public boolean sendMessage(UUID player, String channel, byte[] data) {
        return sendMessage(player, false, channel, data);
    }

    public boolean sendMessage(UUID player, boolean toServer, String channel, byte[] data) {
        if (!toServer) {
            return sendMessage(player, channel, data);
        }
        throw new IllegalStateException("Cannot send plugin message to server on a non-proxy platform");
    }
}
