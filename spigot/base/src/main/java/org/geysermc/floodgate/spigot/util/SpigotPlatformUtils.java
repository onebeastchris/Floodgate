/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.util;

import jakarta.inject.Singleton;
import org.bukkit.Bukkit;
import org.geysermc.floodgate.core.platform.util.PlatformUtils;

@Singleton
public final class SpigotPlatformUtils extends PlatformUtils {
    @Override
    public AuthType authType() {
        if (Bukkit.getOnlineMode()) {
            return AuthType.ONLINE;
        }
        return ProxyUtils.isProxyData() ? AuthType.PROXIED : AuthType.OFFLINE;
    }

    @Override
    public String minecraftVersion() {
        return Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0];
    }

    @Override
    public String serverImplementationName() {
        return Bukkit.getServer().getName();
    }
}
