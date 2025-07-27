/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.command.main;

import jakarta.inject.Singleton;
import org.geysermc.floodgate.core.command.util.Permission;
import org.geysermc.floodgate.core.platform.command.FloodgateCommand;
import org.geysermc.floodgate.core.platform.command.SubCommands;

@Singleton
public final class MainCommand extends SubCommands implements FloodgateCommand {
    MainCommand() {
        super("floodgate", "A set of Floodgate related actions in one command", Permission.COMMAND_MAIN);
    }
}
