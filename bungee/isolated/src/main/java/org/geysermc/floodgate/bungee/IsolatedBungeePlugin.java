/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee;

import java.util.List;
import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.floodgate.isolation.loader.PlatformHolder;
import org.geysermc.floodgate.isolation.loader.PlatformLoader;

public final class IsolatedBungeePlugin extends Plugin {
    private PlatformHolder holder;

    @Override
    public void onLoad() {
        try {
            var libsDirectory = getDataFolder().toPath().resolve("libs");
            holder = PlatformLoader.loadDefault(getClass().getClassLoader(), libsDirectory);
            holder.init(List.of(Plugin.class), List.of(this));
            holder.load();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to load Floodgate", exception);
        }
    }

    @Override
    public void onEnable() {
        holder.enable();
    }

    @Override
    public void onDisable() {
        holder.shutdown();
    }
}
