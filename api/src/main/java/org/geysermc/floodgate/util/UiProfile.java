/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.util;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public enum UiProfile {
    CLASSIC,
    POCKET;

    private static final UiProfile[] VALUES = values();

    /**
     * Get the UiProfile instance from the identifier.
     *
     * @param id the UiProfile identifier
     * @return The UiProfile or {@link #CLASSIC} if the UiProfile wasn't found
     */
    public static UiProfile fromId(int id) {
        return VALUES.length > id ? VALUES[id] : VALUES[0];
    }
}
