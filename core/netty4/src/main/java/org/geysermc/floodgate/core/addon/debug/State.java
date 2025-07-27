/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.debug;

public enum State {
    HANDSHAKE,
    STATUS,
    LOGIN,
    PLAY;

    private static final State[] VALUES = values();

    public static State getById(int id) {
        return id < VALUES.length ? VALUES[id] : null;
    }
}
