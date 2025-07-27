/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.audience;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.floodgate.core.platform.command.CommandUtil;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessor;

/**
 * Command preprocessor which decorated incoming {@link org.incendo.cloud.context.CommandContext}
 * with Floodgate specific objects
 *
 * @param <C> Command sender type
 * @since 2.0
 */
@RequiredArgsConstructor
public final class FloodgateCommandPreprocessor<C> implements CommandPreprocessor<C> {
    private final CommandUtil commandUtil;

    @Override
    public void accept(@NonNull CommandPreprocessingContext<C> context) {
        context.commandContext().store("CommandUtil", commandUtil);
    }
}
