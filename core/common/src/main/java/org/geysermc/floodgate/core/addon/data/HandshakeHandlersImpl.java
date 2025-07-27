/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import jakarta.inject.Singleton;
import java.util.Random;
import org.geysermc.floodgate.core.api.handshake.HandshakeData;
import org.geysermc.floodgate.core.api.handshake.HandshakeHandler;
import org.geysermc.floodgate.core.api.handshake.HandshakeHandlers;

@Singleton
public class HandshakeHandlersImpl implements HandshakeHandlers {
    private final Random random = new Random();
    private final Int2ObjectMap<HandshakeHandler> handshakeHandlers = new Int2ObjectOpenHashMap<>();

    @Override
    public int addHandshakeHandler(HandshakeHandler handshakeHandler) {
        if (handshakeHandler == null) {
            return -1;
        }

        int key;
        do {
            key = random.nextInt(Integer.MAX_VALUE);
        } while (handshakeHandlers.putIfAbsent(key, handshakeHandler) != null);

        return key;
    }

    @Override
    public void removeHandshakeHandler(int handshakeHandlerId) {
        // key is always positive
        if (handshakeHandlerId <= 0) {
            return;
        }

        handshakeHandlers.remove(handshakeHandlerId);
    }

    @Override
    public void removeHandshakeHandler(Class<? extends HandshakeHandler> handshakeHandler) {
        if (HandshakeHandler.class == handshakeHandler) {
            return;
        }

        handshakeHandlers.values().removeIf(handler -> handler.getClass() == handshakeHandler);
    }

    public void callHandshakeHandlers(HandshakeData handshakeData) {
        for (HandshakeHandler handshakeHandler : handshakeHandlers.values()) {
            handshakeHandler.handle(handshakeData);
        }
    }
}
