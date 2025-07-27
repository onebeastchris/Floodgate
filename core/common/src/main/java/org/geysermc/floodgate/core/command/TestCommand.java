/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.command;

import static org.incendo.cloud.parser.standard.StringParser.stringParser;
import static org.incendo.cloud.parser.standard.UUIDParser.uuidParser;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.concurrent.ExecutionException;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.geysermc.floodgate.core.connection.audience.UserAudience.ConsoleAudience;
import org.geysermc.floodgate.core.link.CommonPlayerLink;
import org.geysermc.floodgate.core.platform.command.FloodgateCommand;
import org.geysermc.floodgate.core.util.Constants;
import org.geysermc.floodgate.core.util.Utils;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

@Singleton
public class TestCommand implements FloodgateCommand {
    @Inject
    CommonPlayerLink link;

    @Override
    public Command<ConsoleAudience> buildCommand(CommandManager<UserAudience> commandManager) {
        return commandManager
                .commandBuilder("floodgate-test")
                .senderType(ConsoleAudience.class)
                .required("xuid", stringParser())
                .required("uuid", uuidParser())
                .required("name", stringParser())
                .handler(this::execute)
                .build();
    }

    public void execute(CommandContext<ConsoleAudience> context) {
        try {
            link.addLink(context.get("uuid"), context.get("name"), Utils.toFloodgateUniqueId(context.get("xuid")))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldRegister(FloodgateConfig config) {
        return Constants.DEBUG_MODE;
    }
}
