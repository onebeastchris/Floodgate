/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.Qualifier;
import io.micronaut.core.type.Argument;
import io.micronaut.inject.qualifiers.Qualifiers;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.geysermc.api.Geyser;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.InstanceHolder;
import org.geysermc.floodgate.api.event.FloodgateEventBus;
import org.geysermc.floodgate.core.api.inject.PlatformInjector;
import org.geysermc.floodgate.core.config.ConfigLoader;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.database.loader.DatabaseLoader;
import org.geysermc.floodgate.core.event.EventBus;
import org.geysermc.floodgate.core.event.lifecycle.PostEnableEvent;
import org.geysermc.floodgate.core.event.lifecycle.ShutdownEvent;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.platform.CommonPlatformMessages;
import org.geysermc.floodgate.core.platform.command.Placeholder;
import org.geysermc.floodgate.isolation.IsolatedPlatform;
import org.geysermc.floodgate.isolation.library.LibraryManager;

public abstract class FloodgatePlatform implements IsolatedPlatform {
    private static final UUID KEY = UUID.randomUUID();

    private final LibraryManager manager;
    private ApplicationContext context;
    private PlatformInjector injector;

    protected FloodgatePlatform(LibraryManager manager) {
        this.manager = manager;
    }

    protected void onContextCreated(ApplicationContext context) {}

    @Override
    public void load() {
        long startTime = System.currentTimeMillis();

        context = ApplicationContext.builder(manager.classLoader())
                .singletons(manager)
                .properties(Map.of("platform.proxy", isProxy()))
                .environmentPropertySource(false)
                .eagerInitSingletons(true)
                .build();
        onContextCreated(context);

        // load and register config and database related stuff
        var dataDirectory = context.getBean(Path.class, Qualifiers.byName("dataDirectory"));
        FloodgateConfig config = ConfigLoader.load(dataDirectory, isProxy(), context);
        DatabaseLoader.load(config, manager, dataDirectory, context);

        context.start();

        injector = context.getBean(PlatformInjector.class);

        GeyserApiBase api = context.getBean(GeyserApiBase.class);
        InstanceHolder.set(
                context.getBean(FloodgateApi.class),
                null, // todo context.getBean(PlayerLink.class),
                context.getBean(FloodgateEventBus.class),
                KEY);
        Geyser.set(api);

        long endTime = System.currentTimeMillis();
        context.getBean(FloodgateLogger.class)
                .translatedInfo(
                        CommonPlatformMessages.CORE_FINISH, Placeholder.literal("time_in_ms", endTime - startTime));
    }

    @Override
    public void enable() throws RuntimeException {
        if (injector == null) {
            throw new RuntimeException("Failed to find the platform injector!");
        }

        try {
            injector.inject();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to inject the packet listener!", exception);
        }

        context.getBean(EventBus.class).fire(new PostEnableEvent());
    }

    @Override
    public void disable() {
        // could be used to impl reloading; not relevant for floodgate otherwise
    }

    @Override
    public void shutdown() {
        context.getBean(EventBus.class).fire(new ShutdownEvent());

        if (injector != null && injector.canRemoveInjection()) {
            try {
                injector.removeInjection();
            } catch (Exception exception) {
                throw new RuntimeException("Failed to remove the injection!", exception);
            }
        }

        context.close();
    }

    public abstract boolean isProxy();

    public <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public <T> T getBean(Class<T> clazz, Qualifier<T> qualifier) {
        return context.getBean(clazz, qualifier);
    }

    public <T, R extends T> R getBean(Argument<T> clazz, Qualifier<T> qualifier) {
        //noinspection unchecked
        return (R) context.getBean(clazz, qualifier);
    }
}
