/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.handshake;

/**
 * This class allows you to change and/or get specific data of the Bedrock client before Floodgate
 * does something with this data. This means that Floodgate decrypts the data, then calls the
 * handshake handlers and then applies the data to the connection.<br>
 * <br>
 * /!\ Note that this class will be called for both Java and Bedrock connections, but {@link
 * HandshakeData#isFloodgatePlayer()} will be false and Floodgate related methods will return null
 * for Java players
 */
@Deprecated
@FunctionalInterface
public interface HandshakeHandler {
    /**
     * Method that will be called during the time that Floodgate handles the handshake.
     *
     * @param data the data usable during the handshake
     */
    void handle(HandshakeData data);
}
