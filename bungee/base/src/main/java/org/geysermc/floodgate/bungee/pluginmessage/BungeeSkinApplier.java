/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.bungee.pluginmessage;

import static java.util.Objects.requireNonNull;
import static org.geysermc.floodgate.core.util.ReflectionUtils.getFieldOfType;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.protocol.Property;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;
import org.geysermc.floodgate.core.event.EventBus;
import org.geysermc.floodgate.core.event.skin.SkinApplyEventImpl;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.skin.SkinApplier;
import org.geysermc.floodgate.core.skin.SkinDataImpl;
import org.geysermc.floodgate.core.util.ReflectionUtils;

@Singleton
public final class BungeeSkinApplier implements SkinApplier {
    private static final Field LOGIN_RESULT_FIELD;

    static {
        LOGIN_RESULT_FIELD = getFieldOfType(InitialHandler.class, LoginResult.class);
        requireNonNull(LOGIN_RESULT_FIELD, "LoginResult field cannot be null");
    }

    private final ProxyServer server = ProxyServer.getInstance();

    @Inject
    EventBus eventBus;

    @Inject
    FloodgateLogger logger;

    @Override
    public void applySkin(@NonNull Connection connection, @NonNull SkinData skinData) {
        ProxiedPlayer player = server.getPlayer(connection.javaUuid());
        if (player == null) {
            return;
        }

        InitialHandler handler;
        try {
            handler = (InitialHandler) player.getPendingConnection();
        } catch (Exception exception) {
            logger.error("Incompatible Bungeecord fork detected", exception);
            return;
        }

        LoginResult loginResult = handler.getLoginProfile();
        // expected to be null since LoginResult is the data from hasJoined,
        // which Floodgate players don't have
        if (loginResult == null) {
            // id and name are unused
            loginResult = new LoginResult(null, null, new Property[0]);
            ReflectionUtils.setValue(handler, LOGIN_RESULT_FIELD, loginResult);
        }

        Property[] properties = loginResult.getProperties();

        SkinData currentSkin = currentSkin(properties);

        SkinApplyEvent event = new SkinApplyEventImpl(connection, currentSkin, skinData);
        eventBus.fire(event);

        if (event.cancelled()) {
            return;
        }

        loginResult.setProperties(replaceSkin(properties, event.newSkin()));
    }

    private SkinData currentSkin(Property[] properties) {
        for (Property property : properties) {
            if (property.getName().equals("textures")) {
                if (!property.getValue().isEmpty()) {
                    return new SkinDataImpl(property.getValue(), property.getSignature());
                }
            }
        }
        return null;
    }

    private Property[] replaceSkin(Property[] properties, SkinData skinData) {
        List<Property> list = new ArrayList<>();
        for (Property property : properties) {
            if (!property.getName().equals("textures")) {
                list.add(property);
            }
        }
        list.add(new Property("textures", skinData.value(), skinData.signature()));
        return list.toArray(new Property[0]);
    }
}
