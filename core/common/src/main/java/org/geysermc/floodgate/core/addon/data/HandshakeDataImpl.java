/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.addon.data;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.floodgate.core.api.handshake.HandshakeData;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.FloodgateConnection;
import org.geysermc.floodgate.core.connection.standalone.StandaloneFloodgateConnectionBuilder;
import org.geysermc.floodgate.core.util.Utils;
import org.geysermc.floodgate.util.BedrockData;
import org.geysermc.floodgate.util.LinkedPlayer;

@Getter
public class HandshakeDataImpl implements HandshakeData {
    private final boolean floodgatePlayer;
    private final BedrockData bedrockData;
    private final String javaUsername;
    private final UUID javaUniqueId;

    @Setter
    private LinkedPlayer linkedPlayer;

    @Setter
    private String hostname;

    @Setter
    private String ip;

    @Setter
    private String disconnectReason;

    public HandshakeDataImpl(FloodgateConnection connection, String hostname) {
        this.floodgatePlayer = connection != null;
        this.hostname = hostname;

        BedrockData bedrockData = null;
        LinkedPlayer linkedPlayer = null;
        String javaUsername = null;
        UUID javaUniqueId = null;

        if (connection != null) {
            bedrockData = connection.toBedrockData();
            linkedPlayer = connection.linkedPlayer();

            javaUsername = connection.javaUsername();
            javaUniqueId = Utils.toFloodgateUniqueId(bedrockData.getXuid());
            this.ip = bedrockData.getIp();
        }

        this.bedrockData = bedrockData;
        this.linkedPlayer = linkedPlayer;
        this.javaUsername = javaUsername;
        this.javaUniqueId = javaUniqueId;
    }

    public FloodgateConnection applyChanges(FloodgateConnection connection, FloodgateConfig config) {
        var newLink = !Objects.equals(connection.linkedPlayer(), this.linkedPlayer) ? this.linkedPlayer : null;

        var thisIp = convertIp(this.ip);
        var newIp = !connection.ip().equals(thisIp) ? thisIp : null;

        // only change when there have been any changes
        if (newLink == null && newIp == null) {
            return connection;
        }

        var builder = new StandaloneFloodgateConnectionBuilder(config);
        //        connection.fillBuilder(builder); todo probably remove handshake handlers all together
        if (newLink != null) builder.linkedPlayer(newLink);
        if (newIp != null) builder.ip(newIp);
        return builder.build();
    }

    @Override
    public String getCorrectUsername() {
        return linkedPlayer != null ? linkedPlayer.getJavaUsername() : javaUsername;
    }

    @Override
    public UUID getCorrectUniqueId() {
        return linkedPlayer != null ? linkedPlayer.getJavaUniqueId() : javaUniqueId;
    }

    private static InetAddress convertIp(String ip) {
        String[] sections = ip.split("\\.");
        // if not ipv4, expect ipv6
        if (sections.length == 1) {
            sections = ip.split(":");
        }

        byte[] addressBytes = new byte[sections.length];
        for (int i = 0; i < sections.length; i++) {
            addressBytes[i] = (byte) Integer.parseInt(sections[i]);
        }

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException exception) {
            throw new RuntimeException(exception);
        }
    }
}
