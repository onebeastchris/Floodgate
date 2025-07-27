/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee.module;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.floodgate.core.connection.audience.FloodgateCommandPreprocessor;
import org.geysermc.floodgate.core.connection.audience.FloodgateSenderMapper;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.geysermc.floodgate.core.platform.command.CommandUtil;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.bungee.BungeeCommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;

@Factory
public final class BungeePlatformModule {
    @Bean
    @Singleton
    public CommandManager<UserAudience> commandManager(CommandUtil commandUtil, Plugin plugin) {
        var commandManager = new BungeeCommandManager<>(
                plugin, ExecutionCoordinator.simpleCoordinator(), new FloodgateSenderMapper<>(commandUtil));
        commandManager.registerCommandPreProcessor(new FloodgateCommandPreprocessor<>(commandUtil));
        return commandManager;
    }

    @Bean
    @Named("packetEncoder")
    @Singleton
    public String packetEncoder() {
        return "packet-encoder";
    }

    @Bean
    @Named("packetDecoder")
    @Singleton
    public String packetDecoder() {
        return "packet-decoder";
    }

    @Bean
    @Named("packetHandler")
    @Singleton
    public String packetHandler() {
        return "inbound-boss";
    }

    @Bean
    @Named("implementationName")
    @Singleton
    public String implementationName() {
        return "Bungeecord";
    }
}
