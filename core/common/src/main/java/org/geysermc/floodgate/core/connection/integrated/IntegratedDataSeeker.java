/*
 * Copyright (c) 2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.integrated;

import org.geysermc.floodgate.core.connection.DataSeeker;
import org.geysermc.floodgate.core.connection.FloodgateConnection;

public interface IntegratedDataSeeker extends DataSeeker {
    void addConnection(Object identifier, FloodgateConnection connection);
}
