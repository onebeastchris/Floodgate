/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee;

import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import java.nio.file.Path;
import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.floodgate.core.FloodgatePlatform;
import org.geysermc.floodgate.core.util.ReflectionUtils;
import org.geysermc.floodgate.isolation.library.LibraryManager;
import org.slf4j.LoggerFactory;

public class BungeePlatform extends FloodgatePlatform {
    private final Plugin plugin;

    public BungeePlatform(LibraryManager manager, Plugin plugin) {
        super(manager);
        this.plugin = plugin;
        ReflectionUtils.setPrefix("net.md_5.bungee");
    }

    @Override
    protected void onContextCreated(ApplicationContext context) {
        context.registerSingleton(plugin)
                .registerSingleton(plugin.getProxy())
                .registerSingleton(LoggerFactory.getLogger(BungeePlatform.class))
                .registerSingleton(Path.class, plugin.getDataFolder().toPath(), Qualifiers.byName("dataDirectory"));
    }

    @Override
    public boolean isProxy() {
        return true;
    }
}
