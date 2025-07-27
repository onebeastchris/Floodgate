/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.lang.reflect.Method;
import java.util.UUID;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.core.api.packet.PacketHandler;
import org.geysermc.floodgate.core.api.packet.PacketHandlers;
import org.geysermc.floodgate.core.util.ReflectionUtils;

public class SpigotProtocolSupportHandler implements PacketHandler<ChannelHandlerContext> {
    private static final Method getFromChannel;
    private static final Method getLoginProfile;

    private static final Method setName;
    private static final Method setOriginalName;
    private static final Method setUuid;
    private static final Method setOriginalUuid;

    private static final Method getNetworkManagerWrapper;
    private static final Method getPacketListener;
    private static final Method handleLoginStart;

    static {
        Class<?> connectionImpl = ReflectionUtils.getClass("protocolsupport.protocol.ConnectionImpl");

        getFromChannel = ReflectionUtils.getMethod(connectionImpl, "getFromChannel", Channel.class);
        getLoginProfile = ReflectionUtils.getMethod(connectionImpl, "getLoginProfile");

        Class<?> profile = ReflectionUtils.getClass("protocolsupport.protocol.utils.authlib.LoginProfile");

        setName = ReflectionUtils.getMethod(profile, "setName", String.class);
        setOriginalName = ReflectionUtils.getMethod(profile, "setOriginalName", String.class);
        setUuid = ReflectionUtils.getMethod(profile, "setUUID", UUID.class);
        setOriginalUuid = ReflectionUtils.getMethod(profile, "setOriginalUUID", UUID.class);

        getNetworkManagerWrapper = ReflectionUtils.getMethod(connectionImpl, "getNetworkManagerWrapper");

        Class<?> networkManagerWrapper =
                ReflectionUtils.getClass("protocolsupport.zplatform.network.NetworkManagerWrapper");

        getPacketListener = ReflectionUtils.getMethod(networkManagerWrapper, "getPacketListener");

        Class<?> loginListener =
                ReflectionUtils.getClass("protocolsupport.protocol.packet.handler.AbstractLoginListener");

        handleLoginStart = ReflectionUtils.getMethod(loginListener, "handleLoginStart", String.class);
    }

    @Inject
    @Named("connectionAttribute")
    AttributeKey<Connection> connectionAttribute;

    @Inject
    public void register(PacketHandlers<ChannelHandlerContext> packetHandlers) {
        packetHandlers.register(this, ClassNames.LOGIN_START_PACKET);
    }

    @Override
    public Object handle(ChannelHandlerContext ctx, Object packet, boolean serverbound) {
        Connection player = ctx.channel().attr(connectionAttribute).get();
        if (player == null) {
            return packet;
        }

        Object connection = ReflectionUtils.invoke(null, getFromChannel, ctx.channel());
        Object profile = ReflectionUtils.invoke(connection, getLoginProfile);

        // set correct uuid and name on ProtocolSupport's end, since we skip the LoginStart
        ReflectionUtils.invoke(profile, setName, player.javaUsername());
        ReflectionUtils.invoke(profile, setOriginalName, player.javaUsername());
        ReflectionUtils.invoke(profile, setUuid, player.javaUuid());
        ReflectionUtils.invoke(profile, setOriginalUuid, player.javaUuid());

        Object temp = ReflectionUtils.invoke(connection, getNetworkManagerWrapper);
        temp = ReflectionUtils.invoke(temp, getPacketListener);
        ReflectionUtils.invoke(temp, handleLoginStart, player.javaUsername());
        return packet;
    }
}
