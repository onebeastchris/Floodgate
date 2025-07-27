/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.isolation.loader;

import java.lang.reflect.InvocationTargetException;

public class LoaderUtil {
    public static void invokeLoad(Object platform) {
        try {
            platform.getClass().getMethod("load").invoke(platform);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void invokeEnable(Object platform) {
        try {
            platform.getClass().getMethod("enable").invoke(platform);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void invokeDisable(Object platform) {
        try {
            platform.getClass().getMethod("disable").invoke(platform);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void invokeShutdown(Object platform) {
        try {
            platform.getClass().getMethod("shutdown").invoke(platform);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }
}
