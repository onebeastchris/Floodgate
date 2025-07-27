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
public enum InputMode {
    UNKNOWN,
    KEYBOARD_MOUSE,
    TOUCH,
    CONTROLLER,
    VR;

    private static final InputMode[] VALUES = values();

    /**
     * Get the InputMode instance from the identifier.
     *
     * @param id the InputMode identifier
     * @return The InputMode or {@link #UNKNOWN} if the DeviceOs wasn't found
     */
    public static InputMode fromId(int id) {
        return VALUES.length > id ? VALUES[id] : VALUES[0];
    }
}
