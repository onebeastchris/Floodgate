/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.api;

import java.util.UUID;
import lombok.Getter;
import org.geysermc.floodgate.api.event.FloodgateEventBus;
import org.geysermc.floodgate.api.link.PlayerLink;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public final class InstanceHolder {
    @Getter
    private static FloodgateApi api;

    @Getter
    private static PlayerLink playerLink;

    @Getter
    private static FloodgateEventBus eventBus;

    private static UUID storedKey;

    public static boolean set(
            FloodgateApi floodgateApi, PlayerLink link, FloodgateEventBus floodgateEventBus, UUID key) {
        if (storedKey != null) {
            if (!storedKey.equals(key)) {
                return false;
            }
        } else {
            storedKey = key;
        }

        api = floodgateApi;
        playerLink = link;
        eventBus = floodgateEventBus;
        return true;
    }
}
