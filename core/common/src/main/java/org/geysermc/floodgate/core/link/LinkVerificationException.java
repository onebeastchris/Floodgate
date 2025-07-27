/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.link;

import static org.geysermc.floodgate.core.platform.command.Placeholder.literal;

import org.geysermc.floodgate.core.command.LinkAccountCommand.Message;
import org.geysermc.floodgate.core.platform.command.Placeholder;
import org.geysermc.floodgate.core.platform.command.TranslatableMessage;

public class LinkVerificationException extends RuntimeException {
    public static final LinkVerificationException NO_LINK_REQUESTED =
            new LinkVerificationException(Message.NO_LINK_REQUESTED, literal("command", "/linkaccount"));
    public static final LinkVerificationException INVALID_CODE =
            new LinkVerificationException(Message.INVALID_CODE, literal("command", "/linkaccount"));
    public static final LinkVerificationException LINK_REQUEST_EXPIRED =
            new LinkVerificationException(Message.LINK_REQUEST_EXPIRED, literal("command", "/linkaccount"));

    private final TranslatableMessage message;
    private final Placeholder[] placeholders;

    private LinkVerificationException(TranslatableMessage message, Placeholder... placeholders) {
        super(null, null, true, false);
        this.message = message;
        this.placeholders = placeholders;
    }

    public TranslatableMessage message() {
        return message;
    }

    public Placeholder[] placeholders() {
        return placeholders;
    }
}
