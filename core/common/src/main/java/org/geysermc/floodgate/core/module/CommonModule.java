/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.module;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.geysermc.floodgate.core.crypto.DataCodecType;
import org.geysermc.floodgate.core.crypto.topping.Base64Topping;
import org.geysermc.floodgate.core.crypto.topping.Topping;

@Factory
public final class CommonModule {
    @Bean(preDestroy = "shutdown")
    @Singleton
    @Named("commonPool")
    public ExecutorService commonPool() {
        return new ThreadPoolExecutor(0, 20, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    @Bean(preDestroy = "shutdown")
    @Singleton
    @Named("commonScheduledPool")
    public ScheduledExecutorService commonScheduledPool() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean
    @Singleton
    public DataCodecType codecType() {
        // todo make it a config option and remove this one
        return DataCodecType.AES;
    }

    @Bean
    @Singleton
    public Topping topping() {
        return new Base64Topping();
    }
}
