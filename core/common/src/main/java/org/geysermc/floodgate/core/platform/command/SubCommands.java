/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.command;

import static org.incendo.cloud.description.Description.description;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.geysermc.floodgate.core.command.util.Permission;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

public abstract class SubCommands implements FloodgateCommand {
    private final String name;
    private final String description;
    private final Permission permission;

    @Inject
    Set<FloodgateSubCommand> subCommands;

    protected SubCommands(String name, String description, Permission permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    @Override
    public Command<UserAudience> buildCommand(CommandManager<UserAudience> commandManager) {
        var builder = commandManager
                .commandBuilder(name, description(description))
                .senderType(UserAudience.class)
                .permission(permission.get())
                .handler(this::execute);

        for (FloodgateSubCommand command : subCommands) {
            commandManager.command(command.onBuild(builder));
        }

        // also register /floodgate itself
        return builder.build();
    }

    public void execute(CommandContext<UserAudience> context) {
        var helpMessage = Component.text("Available subcommands are:").appendNewline(); // todo add translation

        // todo this should probably follow MessageType as well
        for (FloodgateSubCommand subCommand : subCommands) {
            var permission = subCommand.permission();
            if (permission == null || context.sender().hasPermission(permission.get())) {
                var component = Component.newline()
                        .append(Component.text(subCommand.name(), NamedTextColor.AQUA))
                        .append(Component.text(" - "))
                        .append(Component.text(subCommand.description(), NamedTextColor.GRAY));
                helpMessage = helpMessage.append(component);
            }
        }

        context.sender().sendMessage(helpMessage);
    }

    @PostConstruct
    public void setup() {
        subCommands.removeIf(subCommand -> !subCommand.parent().isAssignableFrom(this.getClass()));
    }
}
