/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.config;

import io.micronaut.context.env.PropertySource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.util.NamingSchemes;

public final class ConfigAsPropertySource {
    private ConfigAsPropertySource() {}

    public static PropertySource toPropertySource(ConfigurationNode rootNode) {
        Objects.requireNonNull(rootNode);

        var root = CommentedConfigurationNode.root();
        root.node("config").from(rootNode.copy());

        return PropertySource.of(flatten(root));
    }

    private static Map<String, Object> flatten(ConfigurationNode node) {
        var result = new HashMap<String, Object>();
        node.childrenMap().values().forEach(value -> {
            // we expect the properties in camelCase
            var key = NamingSchemes.CAMEL_CASE.coerce(value.key().toString());

            if (value.isMap()) {
                flatten(value).forEach((childKey, childValue) -> result.put(key + "." + childKey, childValue));
                return;
            }
            result.put(key, value.raw());
        });
        return result;
    }
}
