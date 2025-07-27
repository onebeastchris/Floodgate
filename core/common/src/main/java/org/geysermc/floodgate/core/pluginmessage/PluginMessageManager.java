/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.pluginmessage;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class PluginMessageManager {
    private final Map<Class<? extends PluginMessageChannel>, PluginMessageChannel> classInstanceMap = new HashMap<>();
    private final Map<String, PluginMessageChannel> identifierInstanceMap = new HashMap<>();

    @Inject
    PluginMessageRegistration registration;

    @Inject
    public void registerChannels(Set<PluginMessageChannel> channels) {
        if (!classInstanceMap.isEmpty()) {
            return;
        }

        for (PluginMessageChannel channel : channels) {
            classInstanceMap.put(channel.getClass(), channel);
            identifierInstanceMap.put(channel.getIdentifier(), channel);
            registration.register(channel);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginMessageChannel> T getChannel(Class<T> channelType) {
        return (T) classInstanceMap.get(channelType);
    }

    public PluginMessageChannel getChannel(String identifier) {
        return identifierInstanceMap.get(identifier);
    }
}
