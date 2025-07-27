/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.register;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Set;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.geysermc.floodgate.core.platform.command.FloodgateCommand;
import org.incendo.cloud.CommandManager;

/**
 * This class is responsible for registering commands to the command register of the platform that
 * is currently in use. So that the commands only have to be written once (in the common module) and
 * can be used across all platforms without the need of adding platform specific commands.
 */
@Singleton
public final class CommandRegister {
    @Inject
    CommandManager<UserAudience> commandManager;

    @Inject
    FloodgateConfig config;

    @Inject
    public void registerCommands(Set<FloodgateCommand> foundCommands) {
        for (FloodgateCommand command : foundCommands) {
            if (command.shouldRegister(config)) {
                commandManager.command(command.buildCommand(commandManager));
            }
        }
    }
}
