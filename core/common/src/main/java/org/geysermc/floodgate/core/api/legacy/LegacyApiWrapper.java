/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.legacy;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.api.unsafe.Unsafe;
import org.geysermc.floodgate.core.connection.FloodgateConnection;
import org.geysermc.floodgate.core.http.xbox.GetGamertagResult;
import org.geysermc.floodgate.core.http.xbox.GetXuidResult;
import org.geysermc.floodgate.core.http.xbox.XboxClient;
import org.geysermc.floodgate.core.util.Utils;

@Singleton
public final class LegacyApiWrapper implements FloodgateApi {
    @Inject
    GeyserApiBase apiBase;

    @Inject
    XboxClient xboxClient;

    @Override
    public String getPlayerPrefix() {
        return apiBase.usernamePrefix();
    }

    @Override
    public Collection<FloodgatePlayer> getPlayers() {
        return apiBase.onlineConnections().stream()
                .map(connection -> ((FloodgateConnection) connection).legacySelf())
                .collect(Collectors.toList());
    }

    @Override
    public int getPlayerCount() {
        return apiBase.onlineConnectionsCount();
    }

    @Override
    public boolean isFloodgatePlayer(UUID uuid) {
        return apiBase.isBedrockPlayer(uuid);
    }

    @Override
    public FloodgatePlayer getPlayer(UUID uuid) {
        FloodgateConnection connection = (FloodgateConnection) apiBase.connectionByUuid(uuid);
        if (connection == null) {
            return null;
        }
        return connection.legacySelf();
    }

    @Override
    public UUID createJavaPlayerId(long xuid) {
        return new UUID(0, xuid);
    }

    @Override
    public boolean isFloodgateId(UUID uuid) {
        return Utils.isFloodgateUniqueId(uuid);
    }

    @Override
    public boolean sendForm(UUID uuid, Form form) {
        return apiBase.sendForm(uuid, form);
    }

    @Override
    public boolean sendForm(UUID uuid, FormBuilder<?, ?, ?> formBuilder) {
        return apiBase.sendForm(uuid, formBuilder);
    }

    @Override
    public boolean transferPlayer(UUID uuid, String address, int port) {
        return apiBase.transfer(uuid, address, port);
    }

    @Override
    public CompletableFuture<Long> getXuidFor(String gamertag) {
        return xboxClient.xuidByGamertag(gamertag).thenApply(GetXuidResult::xuid);
    }

    @Override
    public CompletableFuture<String> getGamertagFor(long xuid) {
        return xboxClient.gamertagByXuid(xuid).thenApply(GetGamertagResult::gamertag);
    }

    @Override
    public Unsafe unsafe() {
        return null;
    }
}
