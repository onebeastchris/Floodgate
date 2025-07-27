/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api;

import java.util.UUID;
import org.geysermc.floodgate.api.unsafe.Unsafe;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageManager;
import org.geysermc.floodgate.core.pluginmessage.channel.PacketChannel;

public final class UnsafeFloodgateApi implements Unsafe {
    private final PacketChannel packetChannel;

    UnsafeFloodgateApi(PluginMessageManager pluginMessageManager) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        if (!SimpleFloodgateApi.class.getName().equals(element.getClassName())) {
            throw new IllegalStateException("Use the Floodgate api to get an instance");
        }

        packetChannel = pluginMessageManager.getChannel(PacketChannel.class);
    }

    @Override
    public void sendPacket(UUID bedrockPlayer, int packetId, byte[] packetData) {
        byte[] fullData = new byte[packetData.length + 1];
        fullData[0] = (byte) packetId;
        System.arraycopy(packetData, 0, fullData, 1, packetData.length);

        packetChannel.sendPacket(bedrockPlayer, fullData, this);
    }
}
