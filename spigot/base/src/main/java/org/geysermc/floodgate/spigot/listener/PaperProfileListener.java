/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.listener;

import com.destroystokyo.paper.event.profile.PreFillProfileEvent;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.core.api.SimpleFloodgateApi;
import org.geysermc.floodgate.core.listener.McListener;
import org.geysermc.floodgate.core.util.Constants;

@Requires(classes = PreFillProfileEvent.class)
@Singleton
public final class PaperProfileListener implements Listener, McListener {
    private static final ProfileProperty DEFAULT_TEXTURE_PROPERTY = new ProfileProperty(
            "textures", Constants.DEFAULT_MINECRAFT_JAVA_SKIN_TEXTURE, Constants.DEFAULT_MINECRAFT_JAVA_SKIN_SIGNATURE);

    @Inject
    SimpleFloodgateApi api;

    @EventHandler
    public void onFill(PreFillProfileEvent event) {
        UUID id = event.getPlayerProfile().getId();
        if (id == null) {
            return;
        }

        Connection player = api.connectionByUuid(id);
        if (player == null || player.isLinked()) {
            return;
        }

        // back when this event got added the PlayerProfile class didn't have the
        // hasProperty / hasTextures methods
        if (event.getPlayerProfile().getProperties().stream().anyMatch(prop -> "textures".equals(prop.getName()))) {
            return;
        }

        Set<ProfileProperty> properties = new HashSet<>(event.getPlayerProfile().getProperties());
        properties.add(DEFAULT_TEXTURE_PROPERTY);

        event.setProperties(properties);
    }
}
