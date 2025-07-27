/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.velocity.addon.data;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.MinecraftConnection;
import com.velocitypowered.proxy.connection.backend.VelocityServerConnection;
import com.velocitypowered.proxy.protocol.packet.HandshakePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.core.api.SimpleFloodgateApi;
import org.geysermc.floodgate.core.connection.FloodgateConnection;
import org.geysermc.floodgate.core.crypto.FloodgateDataCodec;

@Singleton
@ChannelHandler.Sharable
@SuppressWarnings("ConstantConditions")
public final class VelocityServerDataHandler extends ChannelOutboundHandlerAdapter {
    @Inject
    SimpleFloodgateApi api;

    @Inject
    FloodgateDataCodec dataCodec;

    @Inject
    @Named("connectionAttribute")
    AttributeKey<Connection> connectionAttribute;

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        if (packet instanceof HandshakePacket handshake) {
            String address = handshake.getServerAddress();

            // The connection to the backend server is not the same connection as the client to the proxy.
            // This gets the client to proxy Connection from the backend server connection.

            // get the FloodgatePlayer from the ConnectedPlayer
            MinecraftConnection minecraftConnection =
                    (MinecraftConnection) ctx.pipeline().get("handler");
            VelocityServerConnection serverConnection = (VelocityServerConnection) minecraftConnection.getAssociation();
            Player velocityPlayer = serverConnection.getPlayer();

            Connection connection = api.connectionByPlatformIdentifier(velocityPlayer);
            if (connection != null) {
                // Player is a Floodgate player
                String encodedData = dataCodec.encodeToString((FloodgateConnection) connection);

                // use the same system that we use on bungee, our data goes before all the other data
                int addressFinished = address.indexOf('\0');
                String originalAddress;
                String remaining;
                if (addressFinished == -1) {
                    // There is no additional data to hook onto.
                    // this is the case for 'no forwarding' and 'modern forwarding'
                    originalAddress = address;
                    remaining = "";
                } else {
                    originalAddress = address.substring(0, addressFinished);
                    remaining = address.substring(addressFinished);
                }

                handshake.setServerAddress(originalAddress + '\0' + encodedData + remaining);
            }

            ctx.pipeline().remove(this);
        }

        ctx.write(packet, promise);
    }
}
