/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.config;

import org.spongepowered.configurate.interfaces.meta.defaults.DefaultBoolean;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

/**
 * The Floodgate configuration used by proxy platforms, currently Velocity and BungeeCord.
 */
@ConfigSerializable
public interface ProxyFloodgateConfig extends FloodgateConfig {
    @Comment(
            """
            Should the proxy send the bedrock player data to the servers it is connecting to?
            This requires Floodgate to be installed on the servers.
            You'll get kicked if you don't use the plugin. The default value is false because of it""")
    @DefaultBoolean
    boolean sendFloodgateData();
}
