/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.command;

import org.geysermc.floodgate.core.platform.command.MessageType;
import org.geysermc.floodgate.core.platform.command.TranslatableMessage;

/**
 * Messages (or part of messages) that are used in two or more commands and thus are 'commonly
 * used'
 */
public class CommonCommandMessage {
    public static final TranslatableMessage LINKING_DISABLED =
            new TranslatableMessage("floodgate.commands.linking_disabled", MessageType.ERROR);
    public static final TranslatableMessage NOT_A_PLAYER =
            new TranslatableMessage("floodgate.commands.not_a_player", MessageType.ERROR);
    public static final TranslatableMessage CHECK_CONSOLE = new TranslatableMessage("floodgate.commands.check_console");
    public static final TranslatableMessage UNEXPECTED_ERROR = new TranslatableMessage(
            "floodgate.commands.unexpected_error " + CommonCommandMessage.CHECK_CONSOLE, MessageType.ERROR);
    public static final TranslatableMessage IS_LINKED_ERROR =
            new TranslatableMessage("floodgate.commands.is_linked_error", MessageType.ERROR);
    public static final TranslatableMessage LOCAL_LINKING_NOTICE =
            new TranslatableMessage("floodgate.commands.local_linking_notice", MessageType.INFO);
    public static final TranslatableMessage GLOBAL_LINKING_NOTICE =
            new TranslatableMessage("floodgate.commands.global_linking_notice", MessageType.INFO);
}
