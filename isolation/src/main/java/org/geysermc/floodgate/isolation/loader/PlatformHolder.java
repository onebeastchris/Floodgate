/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.isolation.loader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.geysermc.floodgate.isolation.library.LibraryManager;

public class PlatformHolder {
    private final Class<?> platformClass;
    private final LibraryManager manager;

    private Object platformInstance;

    public PlatformHolder(Class<?> platformClass, LibraryManager manager) {
        this.platformClass = platformClass;
        this.manager = manager;
    }

    public void init(List<Class<?>> argumentTypes, List<Object> argumentValues)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        argumentTypes = new ArrayList<>(argumentTypes);
        argumentValues = new ArrayList<>(argumentValues);

        // LibraryManager is always the first argument
        argumentTypes.add(0, LibraryManager.class);
        argumentValues.add(0, manager);

        platformInstance = platformClass
                .getConstructor(argumentTypes.toArray(Class[]::new))
                .newInstance(argumentValues.toArray());
    }

    public void load() {
        LoaderUtil.invokeLoad(platformInstance);
    }

    public void enable() {
        LoaderUtil.invokeEnable(platformInstance);
    }

    public void disable() {
        LoaderUtil.invokeDisable(platformInstance);
    }

    public void shutdown() {
        LoaderUtil.invokeDisable(platformInstance);
        close();
    }

    public void close() {
        try {
            manager.classLoader().close();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to close classloader!", exception);
        }
    }

    public PlatformHolder platformInstance(Object platformInstance) {
        if (this.platformInstance == null) {
            this.platformInstance = platformInstance;
        }
        return this;
    }

    public LibraryManager manager() {
        return manager;
    }

    public Class<?> platformClass() {
        return platformClass;
    }
}
