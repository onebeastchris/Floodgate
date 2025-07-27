/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.pluginmessage;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;
import org.geysermc.floodgate.core.event.EventBus;
import org.geysermc.floodgate.core.event.skin.SkinApplyEventImpl;
import org.geysermc.floodgate.core.skin.SkinApplier;
import org.geysermc.floodgate.core.util.ReflectionUtils;
import org.geysermc.floodgate.spigot.util.ClassNames;
import org.geysermc.floodgate.spigot.util.SpigotVersionSpecificMethods;

@Singleton
public final class SpigotSkinApplier implements SkinApplier {
    @Inject
    SpigotVersionSpecificMethods versionSpecificMethods;

    @Inject
    EventBus eventBus;

    @Override
    public void applySkin(@NonNull Connection connection, @NonNull SkinData skinData) {
        applySkin0(connection, skinData, true);
    }

    private void applySkin0(Connection connection, SkinData skinData, boolean firstTry) {
        Player player = Bukkit.getPlayer(connection.javaUuid());

        // player is probably not logged in yet
        if (player == null) {
            if (firstTry) {
                versionSpecificMethods.schedule(() -> applySkin0(connection, skinData, false), 10 * 20);
            }
            return;
        }

        GameProfile profile = ReflectionUtils.castedInvoke(player, ClassNames.GET_PROFILE_METHOD);

        if (profile == null) {
            throw new IllegalStateException("The GameProfile cannot be null! " + player.getName());
        }

        PropertyMap properties = profile.getProperties();

        SkinData currentSkin = versionSpecificMethods.currentSkin(properties);

        SkinApplyEvent event = new SkinApplyEventImpl(connection, currentSkin, skinData);
        event.cancelled(connection.isLinked());

        eventBus.fire(event);

        if (event.cancelled()) {
            return;
        }

        replaceSkin(properties, event.newSkin());

        versionSpecificMethods.maybeSchedule(() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.equals(player) && p.canSee(player)) {
                    versionSpecificMethods.hideAndShowPlayer(p, player);
                }
            }
        });
    }

    private void replaceSkin(PropertyMap properties, SkinData skinData) {
        properties.removeAll("textures");
        Property property = new Property("textures", skinData.value(), skinData.signature());
        properties.put("textures", property);
    }
}
