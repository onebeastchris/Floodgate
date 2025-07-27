/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.logger;

import net.kyori.adventure.text.Component;
import org.geysermc.floodgate.core.platform.command.Placeholder;
import org.geysermc.floodgate.core.platform.command.TranslatableMessage;

public interface FloodgateLogger {
    String LOGGER_NAME = "Floodgate";

    /**
     * Logs an error message to the console, with 0 or more arguments.
     *
     * @param message the message to log to the console
     * @param args    the arguments to fill the missing spots in the message
     */
    void error(String message, Object... args);

    /**
     * Logs an error message to the console, with 0 or more arguments.
     *
     * @param message   the message to log to the console
     * @param throwable the throwable to log
     * @param args      the arguments to fill the missing spots in the message
     */
    void error(String message, Throwable throwable, Object... args);

    /**
     * Logs a warning message to the console, with 0 or more arguments.
     *
     * @param message the message to log to the console
     * @param args    the arguments to fill the missing spots in the message
     */
    void warn(String message, Object... args);

    /**
     * Logs an info message to the console, with 0 or more arguments.
     *
     * @param message the message to log to the console
     * @param args    the arguments to fill the missing spots in the message
     */
    void info(String message, Object... args);

    void info(Component message);

    void translatedInfo(TranslatableMessage message, Placeholder... args);

    /**
     * Logs a debug message to the console, with 0 or more arguments.
     *
     * @param message the message to log to the console
     * @param args    the arguments to fill the missing spots in the message
     */
    void debug(String message, Object... args);

    /**
     * Logs a trace message to the console, with 0 or more arguments.
     *
     * @param message the message to log to the console
     * @param args    the arguments to fill the missing spots in the message
     */
    void trace(String message, Object... args);

    /**
     * Returns true if debugging is enabled
     */
    boolean isDebug();
}
