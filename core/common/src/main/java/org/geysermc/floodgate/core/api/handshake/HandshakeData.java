/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.handshake;

import java.util.UUID;
import org.geysermc.floodgate.util.BedrockData;
import org.geysermc.floodgate.util.LinkedPlayer;

/**
 * For advanced users only! You shouldn't play with this unless you know what you're doing.<br>
 * <br>
 * This class allows you change specific things of a Bedrock player before it is applied to the
 * server. Note that at the time I'm writing this that the HandshakeData is created after requesting
 * the player link. So the link is present here, if applicable.
 */
@Deprecated
public interface HandshakeData {
    /**
     * Returns true if the given player is a Floodgate player, false otherwise.
     */
    boolean isFloodgatePlayer();

    /**
     * Returns the decrypted BedrockData sent by Geyser or null if the player isn't a Floodgate
     * player.
     */
    BedrockData getBedrockData();

    String getJavaUsername();

    String getCorrectUsername();

    UUID getJavaUniqueId();

    UUID getCorrectUniqueId();

    /**
     * Returns the linked account associated with the client or null if the player isn't linked or
     * not a Floodgate player.
     */
    LinkedPlayer getLinkedPlayer();

    /**
     * Set the LinkedPlayer. This will be ignored if the player isn't a Floodgate player
     *
     * @param player the player to use as link
     */
    void setLinkedPlayer(LinkedPlayer player);

    /**
     * Returns the hostname used in the handshake packet. This is the hostname after Floodgate
     * removed the data.
     */
    String getHostname();

    /**
     * Set the hostname of the handshake packet. Changing it here will also change it in the
     * handshake packet.
     *
     * @param hostname the new hostname
     */
    void setHostname(String hostname);

    /**
     * Returns the IP address of the client. The initial value is {@link BedrockData#getIp()} when
     * BedrockData isn't null, or null if BedrockData is null. This method will return the changed
     * IP if it has been changed using {@link #setIp(String)}
     */
    String getIp();

    /**
     * Set the IP address of the connected client. Floodgate doesn't perform any checks if the
     * provided data is valid (hence one of the reasons why this class has been made for advanced
     * users), thank you for not abusing Floodgate's trust in you :)
     *
     * @param address the IP address of the client
     */
    void setIp(String address);

    /**
     * Returns the reason to disconnect the current player.
     */
    String getDisconnectReason();

    /**
     * Set the reason to disconnect the current player.
     *
     * @param reason the reason to disconnect
     */
    void setDisconnectReason(String reason);

    /**
     * Returns if the player should be disconnected
     */
    default boolean shouldDisconnect() {
        return getDisconnectReason() != null;
    }
}
