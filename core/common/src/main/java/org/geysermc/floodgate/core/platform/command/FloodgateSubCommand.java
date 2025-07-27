/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.command;

import java.util.Locale;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.floodgate.core.command.util.Permission;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.description.Description;

public abstract class FloodgateSubCommand {
    private final Class<?> parent;
    private final String name;
    private final String description;
    private final Permission permission;
    private final String[] aliases;

    protected FloodgateSubCommand(
            Class<?> parent, String name, String description, Permission permission, String... aliases) {
        this.parent = Objects.requireNonNull(parent);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.permission = permission;
        this.aliases = Objects.requireNonNull(aliases);
    }

    protected FloodgateSubCommand(Class<?> parent, String name, String description, String... aliases) {
        this(parent, name, description, null, aliases);
    }

    public Command.Builder<UserAudience> onBuild(Command.Builder<UserAudience> commandBuilder) {
        var builder = commandBuilder;
        if (permission != null) {
            builder = builder.permission(permission.get());
        }
        return builder.literal(name.toLowerCase(Locale.ROOT), Description.of(description), aliases)
                .handler(this::execute);
    }

    public abstract void execute(CommandContext<UserAudience> context);

    public Class<?> parent() {
        return parent;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public @Nullable Permission permission() {
        return permission;
    }
}
