/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.pluginmessage.channel;

import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.config.ProxyFloodgateConfig;
import org.geysermc.floodgate.core.pluginmessage.PluginMessageChannel;
import org.geysermc.floodgate.core.skin.SkinApplier;
import org.geysermc.floodgate.core.skin.SkinDataImpl;

public class SkinChannel implements PluginMessageChannel {
    @Inject
    GeyserApiBase api;

    @Inject
    FloodgateConfig config;

    @Inject
    SkinApplier skinApplier;

    @Override
    public String getIdentifier() {
        return "floodgate:skin";
    }

    @Override
    public Result handleProxyCall(byte[] data, UUID sourceUuid, String sourceUsername, Identity sourceIdentity) {
        // we can only get skins from Geyser (client)
        if (sourceIdentity == Identity.PLAYER) {
            Result result = handleServerCall(data, sourceUuid, sourceUsername);
            // aka translate 'handled' into 'forward' when send-floodgate-data is enabled
            if (!result.isAllowed() && result.getReason() == null) {
                if (config.proxy() && ((ProxyFloodgateConfig) config).sendFloodgateData()) {
                    return Result.forward();
                }
            }
            return result;
        }

        // Servers can't send skin data
        if (sourceIdentity == Identity.SERVER) {
            return Result.kick("Got skin data from a server?");
        }
        return Result.handled();
    }

    @Override
    public Result handleServerCall(byte[] data, UUID playerUuid, String playerUsername) {
        Connection connection = api.connectionByUuid(playerUuid);
        if (connection == null) {
            return Result.kick("Player sent skins data for a non-Floodgate player");
        }

        String message = new String(data, StandardCharsets.UTF_8);

        String[] split = message.split("\0");
        // value and signature
        if (split.length != 2) {
            return Result.kick("Got invalid skin data");
        }

        String value = split[0];
        String signature = split[1];

        SkinData skinData = new SkinDataImpl(value, signature);

        skinApplier.applySkin(connection, skinData);

        return Result.handled();
    }
}
