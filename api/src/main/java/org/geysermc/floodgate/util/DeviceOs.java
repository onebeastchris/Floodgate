/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeviceOs {
    UNKNOWN("Unknown"),
    GOOGLE("Android"),
    IOS("iOS"),
    OSX("macOS"),
    AMAZON("Amazon"),
    GEARVR("Gear VR"),
    HOLOLENS("Hololens"),
    UWP("Windows"),
    WIN32("Windows x86"),
    DEDICATED("Dedicated"),
    TVOS("Apple TV"),
    PS4("PS4"),
    NX("Switch"),
    XBOX("Xbox One"),
    WINDOWS_PHONE("Windows Phone");

    private final String displayName;

    /**
     * Get the DeviceOs instance from the identifier.
     *
     * @param id the DeviceOs identifier
     * @return The DeviceOs or {@link #UNKNOWN} if the DeviceOs wasn't found
     */
    public static DeviceOs fromId(int id) {
        DeviceOs[] VALUES = values();
        return id < VALUES.length ? VALUES[id] : VALUES[0];
    }

    /**
     * @return friendly display name of platform.
     */
    @Override
    public String toString() {
        return displayName;
    }
}
