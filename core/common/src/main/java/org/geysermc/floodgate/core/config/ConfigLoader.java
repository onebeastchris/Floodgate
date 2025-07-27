/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.config;

import static org.spongepowered.configurate.NodePath.path;

import io.micronaut.context.ApplicationContext;
import java.nio.file.Files;
import java.nio.file.Path;
import org.geysermc.floodgate.core.util.Constants;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.interfaces.InterfaceDefaultOptions;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public final class ConfigLoader {
    private ConfigLoader() {}

    @SuppressWarnings("unchecked")
    public static <T extends FloodgateConfig> T load(Path dataDirectory, boolean isProxy, ApplicationContext context) {
        var configClass = isProxy ? ProxyFloodgateConfig.class : FloodgateConfig.class;

        ConfigurationNode node;
        T config;
        try {
            var loader = YamlConfigurationLoader.builder()
                    .path(dataDirectory.resolve("config.yml"))
                    .defaultOptions(InterfaceDefaultOptions::addTo)
                    .build();

            node = loader.load();
            // temp fix for node.virtual() being broken
            var virtual = !Files.exists(dataDirectory.resolve("config.yml"));

            var migrations = ConfigurationTransformation.versionedBuilder()
                    .addVersion(Constants.CONFIG_VERSION, twoToThree())
                    .addVersion(2, oneToTwo())
                    .addVersion(1, zeroToOne())
                    .build();

            var startVersion = migrations.version(node);
            migrations.apply(node);
            var endVersion = migrations.version(node);

            config = (T) node.get(configClass);

            // save default config or save migrated config
            if (virtual || startVersion != endVersion) {
                loader.save(node);
            }
        } catch (ConfigurateException exception) {
            throw new RuntimeException(
                    "Failed to load the config! Try to delete the config file if this error persists", exception);
        }

        // make sure the proxy and the normal config types are registered
        context.registerSingleton(config);
        context.registerSingleton(FloodgateConfig.class, config);
        // make @Requires etc. work
        context.getEnvironment().addPropertySource(ConfigAsPropertySource.toPropertySource(node));
        return config;
    }

    private static ConfigurationTransformation zeroToOne() {
        return ConfigurationTransformation.builder()
                .addAction(path("playerLink", "enable"), (path, value) -> {
                    return new Object[] {"playerLink", "enabled"};
                })
                .addAction(path("playerLink", "allowLinking"), (path, value) -> {
                    return new Object[] {"playerLink", "allowed"};
                })
                .build();
    }

    private static ConfigurationTransformation oneToTwo() {
        return ConfigurationTransformation.builder()
                .addAction(path("playerLink", "useGlobalLinking"), (path, value) -> {
                    return new Object[] {"playerLink", "enableGlobalLinking"};
                })
                .build();
    }

    private static ConfigurationTransformation twoToThree() {
        return ConfigurationTransformation.builder()
                .addAction(path("playerLink", "type"), (path, value) -> {
                    return new Object[] {"database", "type"};
                })
                .build();
    }
}
