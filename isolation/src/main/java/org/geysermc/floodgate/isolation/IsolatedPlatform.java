/*
 * Copyright (c) 2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.isolation;

public interface IsolatedPlatform {
    void load();

    void enable();

    void disable();

    void shutdown();
}
