/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.command;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.floodgate.core.util.LanguageManager;

/**
 * TranslatableMessage is the common class for a message that can be translated
 */
public class TranslatableMessage {
    private final String rawMessage;
    private final String[] translateParts;
    private final MessageType type;

    public TranslatableMessage(String rawMessage, MessageType type) {
        this.rawMessage = rawMessage;
        this.translateParts = rawMessage.split(" ");
        this.type = type;
    }

    public TranslatableMessage(String rawMessage) {
        this(rawMessage, MessageType.NONE);
    }

    public Component translateMessage(LanguageManager manager, @Nullable String locale, Placeholder... placeholders) {
        if (locale == null) {
            locale = manager.getDefaultLocale();
        }

        if (translateParts.length == 1) {
            return manager.getString(rawMessage, locale, type, placeholders);
        }

        Component complete = Component.empty();
        for (int i = 0; i < translateParts.length; i++) {
            if (i != 0) {
                complete = complete.append(Component.text(' '));
            }
            complete = complete.append(manager.getString(translateParts[i], locale, type, placeholders));
        }
        return complete;
    }

    @Override
    public String toString() {
        return rawMessage;
    }
}
