/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.handshake;

/**
 * @deprecated This system has been deprecated and will not be available in the new API that will be
 * introduced when Geyser will include Floodgate (and thus will have some common base API).
 * <br>
 * It might be replaced with an event (probably internal), but that isn't certain yet.
 */
@Deprecated
public interface HandshakeHandlers {
    /**
     * Register a custom handshake handler. This can be used to check and edit the player during the
     * handshake handling.
     *
     * @param handshakeHandler the handshake handler to register
     * @return a random (unique) int to identify this handshake handler or -1 if null
     */
    int addHandshakeHandler(HandshakeHandler handshakeHandler);

    /**
     * Removes a custom handshake handler by id.
     *
     * @param handshakeHandlerId the id of the handshake handler to remove
     */
    void removeHandshakeHandler(int handshakeHandlerId);

    /**
     * Remove a custom handshake handler by instance.
     *
     * @param handshakeHandler the instance to remove
     */
    void removeHandshakeHandler(Class<? extends HandshakeHandler> handshakeHandler);
}
