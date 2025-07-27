/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.inject;

import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.geysermc.floodgate.core.api.inject.InjectorAddon;
import org.geysermc.floodgate.core.api.inject.PlatformInjector;

public abstract class Netty4PlatformInjector implements PlatformInjector<Channel> {
    private final Set<Channel> injectedClients =
            Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    private final Map<Class<?>, InjectorAddon<Channel>> addons = new HashMap<>();

    @Inject
    Set<InjectorAddon<Channel>> detectedAddons;

    @PostConstruct
    public void registerAddons() {
        detectedAddons.forEach(this::addAddon);
    }

    protected void addInjectedClient(Channel channel) {
        injectedClients.add(channel);
    }

    public void removeInjectedClient(Channel channel) {
        injectedClients.remove(channel);
    }

    public Set<Channel> injectedClients() {
        return injectedClients;
    }

    @Override
    public void addAddon(InjectorAddon<Channel> addon) {
        addons.putIfAbsent(addon.getClass(), addon);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends InjectorAddon<Channel>> T removeAddon(Class<T> addon) {
        return (T) addons.remove(addon);
    }

    /**
     * Method to loop through all the addons and call {@link InjectorAddon#onInject(Object,
     * boolean)} if {@link InjectorAddon#shouldInject()}.
     *
     * @param channel       the channel to inject
     * @param proxyToServer true if the proxy is connecting to a server or false when the player is
     *                      connecting to the proxy or false when the platform isn't a proxy
     */
    public void injectAddonsCall(Channel channel, boolean proxyToServer) {
        // don't forget to remove the addons when the channel closes
        channel.closeFuture().addListener(listener -> {
            channelClosedCall(channel);
            removeInjectedClient(channel);
        });

        for (InjectorAddon<Channel> addon : addons.values()) {
            if (addon.shouldInject()) {
                addon.onInject(channel, proxyToServer);
            }
        }
    }

    /**
     * Method to loop through all the addons and call {@link InjectorAddon#onChannelClosed(Object)}
     * if {@link InjectorAddon#shouldInject()}
     *
     * @param channel the channel that was injected
     */
    public void channelClosedCall(Channel channel) {
        for (InjectorAddon<Channel> addon : addons.values()) {
            if (addon.shouldInject()) {
                addon.onChannelClosed(channel);
            }
        }
    }

    /**
     * Method to loop through all the addons and call {@link InjectorAddon#onRemoveInject(Object)}
     * if {@link InjectorAddon#shouldInject()}.
     *
     * @param channel the channel that was injected
     */
    public void removeAddonsCall(Channel channel) {
        for (InjectorAddon<Channel> addon : addons.values()) {
            if (addon.shouldInject()) {
                addon.onRemoveInject(channel);
            }
        }
    }
}
