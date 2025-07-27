/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.isolation.loader.PlatformHolder;
import org.geysermc.floodgate.isolation.loader.PlatformLoader;

public final class IsolatedSpigotPlugin extends JavaPlugin {
    private PlatformHolder holder;

    @Override
    public void onLoad() {
        try {
            var libsDirectory = getDataFolder().toPath().resolve("libs");
            holder = PlatformLoader.loadDefault(getClass().getClassLoader(), libsDirectory);
            holder.init(List.of(JavaPlugin.class), List.of(this));
        } catch (Exception exception) {
            throw new RuntimeException("Failed to load Floodgate", exception);
        }
    }

    @Override
    public void onEnable() {
        holder.load();
        try {
            holder.enable();
        } catch (Exception exception) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw exception;
        }
    }

    @Override
    public void onDisable() {
        holder.shutdown();
    }
}
