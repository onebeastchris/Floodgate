/*
 * Copyright (c) 2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.audience;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.floodgate.core.platform.command.CommandUtil;
import org.incendo.cloud.SenderMapper;

public record FloodgateSenderMapper<T>(CommandUtil commandUtil) implements SenderMapper<T, UserAudience> {
    @Override
    public @NonNull UserAudience map(@NonNull T base) {
        return commandUtil.getUserAudience(base);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull T reverse(@NonNull UserAudience mapped) {
        return (T) mapped.source();
    }
}
