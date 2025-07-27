/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot;

import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import java.nio.file.Path;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.core.FloodgatePlatform;
import org.geysermc.floodgate.isolation.library.LibraryManager;
import org.geysermc.floodgate.spigot.util.SpigotProtocolSupportHandler;
import org.geysermc.floodgate.spigot.util.SpigotProtocolSupportListener;
import org.slf4j.LoggerFactory;

public class SpigotPlatform extends FloodgatePlatform {
    private final JavaPlugin plugin;
    private ApplicationContext context;

    public SpigotPlatform(LibraryManager manager, JavaPlugin plugin) {
        super(manager);
        this.plugin = plugin;
    }

    @Override
    protected void onContextCreated(ApplicationContext context) {
        context.registerSingleton(plugin)
                .registerSingleton(plugin.getServer())
                .registerSingleton(LoggerFactory.getLogger(plugin.getLogger().getName()))
                .registerSingleton(Path.class, plugin.getDataFolder().toPath(), Qualifiers.byName("dataDirectory"));
        this.context = context;
    }

    @Override
    public void load() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException ignored) {
            throw new IllegalStateException(
                    "Floodgate doesn't work on CraftBukkit! Please use Paper or Spigot instead.");
        }

        super.load();
    }

    @Override
    public void enable() throws RuntimeException {
        super.enable();

        // add ProtocolSupport support (hack)
        if (plugin.getServer().getPluginManager().getPlugin("ProtocolSupport") != null) {
            context.getBean(SpigotProtocolSupportHandler.class);
            SpigotProtocolSupportListener.registerHack(plugin);
        }
    }

    @Override
    public boolean isProxy() {
        return false;
    }
}
