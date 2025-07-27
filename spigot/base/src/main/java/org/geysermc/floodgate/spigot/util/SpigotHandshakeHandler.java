/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.util;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.UUID;
import org.geysermc.floodgate.core.api.handshake.HandshakeData;
import org.geysermc.floodgate.core.api.handshake.HandshakeHandler;
import org.geysermc.floodgate.core.api.handshake.HandshakeHandlers;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.util.BedrockData;

@Singleton
public class SpigotHandshakeHandler implements HandshakeHandler {
    @Inject
    FloodgateLogger logger;

    @Inject
    void init(HandshakeHandlers handlers) {
        handlers.addHandshakeHandler(this);
    }

    @Override
    public void handle(HandshakeData data) {
        // we never have to do anything when BedrockData is null.
        // BedrockData is null when something went wrong (e.g. invalid key / exception)
        if (data.getBedrockData() == null) {
            return;
        }

        BedrockData bedrockData = data.getBedrockData();
        UUID correctUuid = data.getCorrectUniqueId();

        // replace the ip and uuid with the Bedrock client IP and an uuid based of the xuid
        String[] split = data.getHostname().split("\0");
        if (split.length >= 3) {
            if (logger.isDebug()) {
                logger.info(
                        "Replacing hostname arg1 '{}' with '{}' and arg2 '{}' with '{}'",
                        split[1],
                        bedrockData.getIp(),
                        split[2],
                        correctUuid.toString());
            }
            split[1] = bedrockData.getIp();
            split[2] = correctUuid.toString();
        }
        data.setHostname(String.join("\0", split));
    }
}
