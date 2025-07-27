/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity.inject;

import static org.geysermc.floodgate.core.util.ReflectionUtils.getCastedValue;
import static org.geysermc.floodgate.core.util.ReflectionUtils.getMethod;
import static org.geysermc.floodgate.core.util.ReflectionUtils.invoke;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.proxy.network.ConnectionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.geysermc.floodgate.core.inject.Netty4PlatformInjector;

@Singleton
public final class VelocityInjector extends Netty4PlatformInjector {
    @Inject
    ProxyServer server;

    @Getter
    private boolean injected;

    @Override
    @SuppressWarnings({"DataFlowIssue", "deprecation"})
    public void inject() {
        if (isInjected()) {
            return;
        }

        ConnectionManager connectionManager = getCastedValue(server, "cm");

        // Client <-> Proxy

        var serverChannelInitializer = connectionManager.serverChannelInitializer;
        serverChannelInitializer.set(new VelocityChannelInitializer(this, serverChannelInitializer.get(), false));

        // Proxy <-> Server

        var backendChannelInitializer = connectionManager.backendChannelInitializer;
        backendChannelInitializer.set(new VelocityChannelInitializer(this, backendChannelInitializer.get(), true));

        injected = true;
    }

    @Override
    public boolean canRemoveInjection() {
        return false;
    }

    @Override
    public void removeInjection() {
        throw new IllegalStateException("Floodgate cannot remove itself from Velocity without a reboot");
    }

    @RequiredArgsConstructor
    @SuppressWarnings("rawtypes")
    private static final class VelocityChannelInitializer extends ChannelInitializer<Channel> {
        private static final Method initChannel;

        static {
            initChannel = getMethod(ChannelInitializer.class, "initChannel", Channel.class);
        }

        private final VelocityInjector injector;
        private final ChannelInitializer original;
        private final boolean proxyToServer;

        @Override
        protected void initChannel(Channel channel) {
            invoke(original, initChannel, channel);

            injector.injectAddonsCall(channel, proxyToServer);
            injector.addInjectedClient(channel);
        }
    }
}
