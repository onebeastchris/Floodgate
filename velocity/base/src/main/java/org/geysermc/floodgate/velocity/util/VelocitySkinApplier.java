/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.GameProfile.Property;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;
import org.geysermc.floodgate.core.event.EventBus;
import org.geysermc.floodgate.core.event.skin.SkinApplyEventImpl;
import org.geysermc.floodgate.core.skin.SkinApplier;
import org.geysermc.floodgate.core.skin.SkinDataImpl;

@Singleton
public class VelocitySkinApplier implements SkinApplier {
    @Inject
    ProxyServer server;

    @Inject
    EventBus eventBus;

    @Override
    public void applySkin(Connection connection, SkinData skinData) {
        server.getPlayer(connection.javaUuid()).ifPresent(player -> {
            List<Property> properties = new ArrayList<>(player.getGameProfileProperties());

            SkinData currentSkin = currentSkin(properties);

            SkinApplyEvent event = new SkinApplyEventImpl(connection, currentSkin, skinData);
            event.cancelled(connection.isLinked());

            eventBus.fire(event);

            if (event.cancelled()) {
                return;
            }

            replaceSkin(properties, event.newSkin());
            player.setGameProfileProperties(properties);
        });
    }

    private SkinData currentSkin(List<Property> properties) {
        for (Property property : properties) {
            if (property.getName().equals("textures")) {
                if (!property.getValue().isEmpty()) {
                    return new SkinDataImpl(property.getValue(), property.getSignature());
                }
            }
        }
        return null;
    }

    private void replaceSkin(List<Property> properties, SkinData skinData) {
        properties.removeIf(property -> property.getName().equals("textures"));
        properties.add(new Property("textures", skinData.value(), skinData.signature()));
    }
}
