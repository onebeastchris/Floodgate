/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.legacy;

import java.util.UUID;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.api.player.PropertyKey;
import org.geysermc.floodgate.core.connection.FloodgateConnection;
import org.geysermc.floodgate.util.DeviceOs;
import org.geysermc.floodgate.util.InputMode;
import org.geysermc.floodgate.util.LinkedPlayer;
import org.geysermc.floodgate.util.UiProfile;

public class LegacyPlayerWrapper implements FloodgatePlayer {
    private final FloodgateConnection connection;
    private final String javaUsername;
    private final UUID javaUniqueId;

    public LegacyPlayerWrapper(FloodgateConnection connection, String javaUsername, UUID javaUniqueId) {
        this.connection = connection;
        this.javaUsername = javaUsername;
        this.javaUniqueId = javaUniqueId;
    }

    @Override
    public String getJavaUsername() {
        return javaUsername;
    }

    @Override
    public UUID getJavaUniqueId() {
        return javaUniqueId;
    }

    @Override
    public UUID getCorrectUniqueId() {
        return connection.javaUuid();
    }

    @Override
    public String getCorrectUsername() {
        return connection.javaUsername();
    }

    @Override
    public String getVersion() {
        return connection.version();
    }

    @Override
    public String getUsername() {
        return connection.bedrockUsername();
    }

    @Override
    public String getXuid() {
        return connection.xuid();
    }

    @Override
    public DeviceOs getDeviceOs() {
        return DeviceOs.fromId(connection.platform().ordinal());
    }

    @Override
    public String getLanguageCode() {
        return connection.languageCode();
    }

    @Override
    public UiProfile getUiProfile() {
        return UiProfile.fromId(connection.uiProfile().ordinal());
    }

    @Override
    public InputMode getInputMode() {
        return InputMode.fromId(connection.inputMode().ordinal());
    }

    @Override
    public boolean isFromProxy() {
        return false; // just as in FloodgateConnection#toBedrockData
    }

    @Override
    public LinkedPlayer getLinkedPlayer() {
        if (isLinked()) {
            return LinkedPlayer.of(
                    connection.javaUsername(),
                    connection.javaUuid(),
                    FloodgateApi.getInstance().createJavaPlayerId(Long.parseLong(getXuid())));
        }
        return null;
    }

    @Override
    public boolean isLinked() {
        return connection.isLinked();
    }

    @Override
    public boolean hasProperty(PropertyKey key) {
        return connection.propertyGlue().hasProperty(key);
    }

    @Override
    public boolean hasProperty(String key) {
        return connection.propertyGlue().hasProperty(key);
    }

    @Override
    public <T> T getProperty(PropertyKey key) {
        return connection.propertyGlue().getProperty(key);
    }

    @Override
    public <T> T getProperty(String key) {
        return connection.propertyGlue().getProperty(key);
    }

    @Override
    public <T> T removeProperty(PropertyKey key) {
        return connection.propertyGlue().removeProperty(key);
    }

    @Override
    public <T> T removeProperty(String key) {
        return connection.propertyGlue().removeProperty(key);
    }

    @Override
    public <T> T addProperty(PropertyKey key, Object value) {
        return connection.propertyGlue().addProperty(key, value);
    }

    @Override
    public <T> T addProperty(String key, Object value) {
        return connection.propertyGlue().addProperty(key, value);
    }
}
