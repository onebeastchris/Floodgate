/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PlatformUtils {
    /**
     * Returns the authentication type used on the platform
     */
    public abstract AuthType authType();

    /**
     * Returns the Minecraft version the server is based on (or the most recent supported version
     * for proxy platforms)
     */
    public abstract String minecraftVersion();

    public abstract String serverImplementationName();

    public enum AuthType {
        ONLINE,
        PROXIED,
        OFFLINE
    }
}
