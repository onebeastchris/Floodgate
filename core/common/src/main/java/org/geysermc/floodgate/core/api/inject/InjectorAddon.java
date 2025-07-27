/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.inject;

public interface InjectorAddon<C> {
    /**
     * Called when injecting a specific channel (every client that is connected to the server has
     * his own channel). Internally used for the Floodgate debugger and data handler but can also be
     * used for third party things.
     *
     * @param channel  the channel that the injector is injecting
     * @param toServer if the connection is between a proxy and a server
     */
    void onInject(C channel, boolean toServer);

    /**
     * Called when the channel has been closed. Note that this method will be called for every
     * closed connection (if it is injected), so it'll also run this method for closed connections
     * between a server and the proxy (when Floodgate is running on a proxy).
     *
     * @param channel the channel that the injector injected
     */
    default void onChannelClosed(C channel) {}

    /**
     * Called when Floodgate is removing the injection from the server. The addon should remove his
     * traces otherwise it is likely that an error will popup after the server is injected again.
     *
     * @param channel the channel that the injector injected
     */
    void onRemoveInject(C channel);

    /**
     * If the Injector should call {@link #onInject(C, boolean)}
     *
     * @return true if it should, false otherwise
     */
    boolean shouldInject();
}
