/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity.module;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.geysermc.floodgate.core.connection.audience.FloodgateCommandPreprocessor;
import org.geysermc.floodgate.core.connection.audience.FloodgateSenderMapper;
import org.geysermc.floodgate.core.connection.audience.UserAudience;
import org.geysermc.floodgate.core.platform.command.CommandUtil;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.VelocityCommandManager;

@Factory
public final class VelocityPlatformModule {
    @Bean
    @Singleton
    public CommandManager<UserAudience> commandManager(
            CommandUtil commandUtil, ProxyServer proxy, PluginContainer container) {
        var commandManager = new VelocityCommandManager<>(
                container, proxy, ExecutionCoordinator.simpleCoordinator(), new FloodgateSenderMapper<>(commandUtil));
        commandManager.registerCommandPreProcessor(new FloodgateCommandPreprocessor<>(commandUtil));
        return commandManager;
    }

    @Bean
    @Named("packetEncoder")
    @Singleton
    public String packetEncoder() {
        return "minecraft-encoder";
    }

    @Bean
    @Named("packetDecoder")
    @Singleton
    public String packetDecoder() {
        return "minecraft-decoder";
    }

    @Bean
    @Named("packetHandler")
    @Singleton
    public String packetHandler() {
        return "handler";
    }

    @Bean
    @Named("implementationName")
    @Singleton
    public String implementationName() {
        return "Velocity";
    }
}
