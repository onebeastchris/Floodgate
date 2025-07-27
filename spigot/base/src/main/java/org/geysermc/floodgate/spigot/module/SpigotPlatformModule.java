/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.module;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Factory
public final class SpigotPlatformModule {
    @Bean
    @Named("packetEncoder")
    @Singleton
    public String packetEncoder() {
        return "encoder";
    }

    @Bean
    @Named("packetDecoder")
    @Singleton
    public String packetDecoder() {
        return "decoder";
    }

    @Bean
    @Named("packetHandler")
    @Singleton
    public String packetHandler() {
        return "packet_handler";
    }

    @Bean
    @Named("implementationName")
    @Singleton
    public String implementationName() {
        return "Spigot";
    }
}
