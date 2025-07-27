/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.listener;

import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Set;
import org.geysermc.floodgate.core.event.lifecycle.PostEnableEvent;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.platform.listener.ListenerRegistration;

@Singleton
@SuppressWarnings({"rawtypes", "unchecked"})
public final class McListenerRegister {
    @Inject
    ListenerRegistration registration;

    @Inject
    FloodgateLogger logger;

    @Inject
    Set<McListener> listeners;

    @EventListener
    public void onPostEnable(PostEnableEvent ignored) {
        listeners.forEach(registration::register);
    }
}
