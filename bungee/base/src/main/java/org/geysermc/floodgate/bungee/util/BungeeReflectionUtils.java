/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

// Reflection just for Bungee because Bungee is special :)
public class BungeeReflectionUtils {
    private static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format(
                            "Cannot initialize required reflection setup :/\nJava version: %s\nVendor: %s (%s)",
                            System.getProperty("java.version"),
                            System.getProperty("java.vendor"),
                            System.getProperty("java.vendor.url")),
                    exception);
        }
    }

    public static void setFieldValue(Object object, Field field, Object result) {
        try {
            boolean isStatic = Modifier.isStatic(field.getModifiers());

            long offset = isStatic ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field);

            if (isStatic) {
                UNSAFE.putObject(UNSAFE.staticFieldBase(field), offset, result);
            } else {
                UNSAFE.putObject(object, offset, result);
            }
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format(
                            "Java version: %s\nVendor: %s (%s)",
                            System.getProperty("java.version"),
                            System.getProperty("java.vendor"),
                            System.getProperty("java.vendor.url")),
                    exception);
        }
    }
}
