/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.util;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinkedPlayer implements Cloneable {
    /**
     * The Java username of the linked player
     */
    private final String javaUsername;
    /**
     * The Java UUID of the linked player
     */
    private final UUID javaUniqueId;
    /**
     * The UUID of the Bedrock player
     */
    private final UUID bedrockId;
    /**
     * If the LinkedPlayer is sent from a different platform. For example the LinkedPlayer is from
     * Bungee but the data has been sent to the Bukkit server.
     */
    private boolean fromDifferentPlatform = false;

    public static LinkedPlayer of(String javaUsername, UUID javaUniqueId, UUID bedrockId) {
        return new LinkedPlayer(javaUsername, javaUniqueId, bedrockId);
    }

    public static LinkedPlayer fromString(String data) {
        String[] split = data.split(";");
        if (split.length != 3) {
            return null;
        }

        LinkedPlayer player = new LinkedPlayer(split[0], UUID.fromString(split[1]), UUID.fromString(split[2]));
        player.fromDifferentPlatform = true;
        return player;
    }

    @Override
    public String toString() {
        return javaUsername + ';' + javaUniqueId.toString() + ';' + bedrockId.toString();
    }

    @Override
    public LinkedPlayer clone() throws CloneNotSupportedException {
        return (LinkedPlayer) super.clone();
    }
}
