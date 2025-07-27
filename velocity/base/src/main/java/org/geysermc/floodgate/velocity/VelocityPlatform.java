/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import java.nio.file.Path;
import org.geysermc.floodgate.core.FloodgatePlatform;
import org.geysermc.floodgate.isolation.library.LibraryManager;
import org.slf4j.Logger;

public class VelocityPlatform extends FloodgatePlatform {
    @Inject
    @DataDirectory
    Path dataDirectory;

    @Inject
    ProxyServer proxyServer;

    @Inject
    EventManager eventManager;

    @Inject
    PluginContainer container;

    @Inject
    Logger logger;

    @Inject
    public VelocityPlatform(LibraryManager manager) {
        super(manager);
    }

    @Override
    protected void onContextCreated(ApplicationContext context) {
        context.registerSingleton(proxyServer)
                .registerSingleton(container)
                .registerSingleton(eventManager)
                .registerSingleton(Path.class, dataDirectory, Qualifiers.byName("dataDirectory"))
                .registerSingleton(logger);
    }

    @Override
    public boolean isProxy() {
        return true;
    }
}
