/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.command;

import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;

/** The base class for every Floodgate command. */
public interface FloodgateCommand {
    /**
     * Called by the CommandRegister when it wants you to build the command which he can add.
     *
     * @param commandManager the manager to create a command
     * @return the command to register
     */
    Command<? extends UserAudience> buildCommand(CommandManager<UserAudience> commandManager);

    /**
     * Called by the CommandRegister to check if the command should be added given the config.
     *
     * @param config the config to check if a command should be added
     * @return true if it should be added
     */
    default boolean shouldRegister(FloodgateConfig config) {
        return true;
    }
}
