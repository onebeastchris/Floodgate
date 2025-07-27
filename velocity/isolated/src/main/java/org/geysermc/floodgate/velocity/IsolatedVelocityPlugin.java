/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.nio.file.Path;
import org.geysermc.floodgate.isolation.library.LibraryManager;
import org.geysermc.floodgate.isolation.loader.PlatformHolder;
import org.geysermc.floodgate.isolation.loader.PlatformLoader;

public final class IsolatedVelocityPlugin {
    private final PlatformHolder holder;

    @Inject
    public IsolatedVelocityPlugin(Injector guice, @DataDirectory Path dataDirectory) {
        try {
            var libsDirectory = dataDirectory.resolve("libs");

            holder = PlatformLoader.loadDefault(getClass().getClassLoader(), libsDirectory);
            Injector child = guice.createChildInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(LibraryManager.class).toInstance(holder.manager());
                }
            });

            holder.platformInstance(child.getInstance(holder.platformClass()));
            holder.load();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to load Floodgate", exception);
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        holder.enable();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        holder.shutdown();
    }
}
