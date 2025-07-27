/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.inject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Getter;
import org.geysermc.floodgate.core.inject.Netty4PlatformInjector;
import org.geysermc.floodgate.core.logger.FloodgateLogger;
import org.geysermc.floodgate.core.util.ReflectionUtils;
import org.geysermc.floodgate.spigot.util.ClassNames;

@Singleton
public final class SpigotInjector extends Netty4PlatformInjector {
    @Inject
    FloodgateLogger logger;

    private Object serverConnection;
    private String injectedFieldName;

    @Getter
    private boolean injected;

    @Override
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void inject() throws Exception {
        if (isInjected()) {
            return;
        }

        Object serverConnection = getServerConnection();
        if (serverConnection == null) {
            throw new RuntimeException("Unable to find server connection");
        }

        for (Field field : serverConnection.getClass().getDeclaredFields()) {
            if (field.getType() == List.class) {
                field.setAccessible(true);

                ParameterizedType parameterType = ((ParameterizedType) field.getGenericType());
                Type listType = parameterType.getActualTypeArguments()[0];

                // the list we search has ChannelFuture as type
                if (listType != ChannelFuture.class) {
                    continue;
                }

                injectedFieldName = field.getName();
                List<?> newList = new CustomList((List<?>) field.get(serverConnection)) {
                    @Override
                    public void onAdd(Object object) {
                        try {
                            injectClient((ChannelFuture) object);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                };

                // inject existing
                synchronized (newList) {
                    for (Object object : newList) {
                        try {
                            injectClient((ChannelFuture) object);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }

                field.set(serverConnection, newList);
                injected = true;
                return;
            }
        }
    }

    public void injectClient(ChannelFuture future) {
        future.channel().pipeline().addFirst("floodgate-init", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                super.channelRead(ctx, msg);

                Channel channel = (Channel) msg;
                channel.pipeline().addLast(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        injectAddonsCall(channel, false);
                        addInjectedClient(channel);
                    }
                });
            }
        });
    }

    @Override
    public void removeInjection() {
        if (!isInjected()) {
            return;
        }

        // let's change the list back to the original first
        // so that new connections are not handled through our custom list
        Object serverConnection = getServerConnection();
        if (serverConnection != null) {
            Field field = ReflectionUtils.getField(serverConnection.getClass(), injectedFieldName);
            Object value = ReflectionUtils.getValue(serverConnection, field);

            if (value instanceof CustomList) {
                // all we have to do is replace the list with the original list.
                // the original list is up-to-date, so we don't have to clear/add/whatever anything
                CustomList customList = (CustomList) value;
                ReflectionUtils.setValue(serverConnection, field, customList.getOriginalList());
                return;
            }

            // we could replace all references of CustomList that are directly in 'value', but that
            // only brings you so far. ProtocolLib for example stores the original value
            // (which would be our CustomList e.g.) in a separate object
            logger.debug(
                    "Unable to remove all references of Floodgate due to {}! ",
                    value.getClass().getName());
        }

        // remove injection from clients
        for (Channel channel : injectedClients()) {
            removeAddonsCall(channel);
        }

        // todo make sure that all references are removed from the channels,
        // except from one AttributeKey with Floodgate player data which could be used
        // after reloading

        injected = false;
    }

    private Object getServerConnection() {
        if (serverConnection != null) {
            return serverConnection;
        }
        Class<?> minecraftServer = ClassNames.MINECRAFT_SERVER;

        // method by CraftBukkit to get the instance of the MinecraftServer
        Object minecraftServerInstance = ReflectionUtils.invokeStatic(minecraftServer, "getServer");

        Method method = ReflectionUtils.getMethodThatReturns(minecraftServer, ClassNames.SERVER_CONNECTION, true);

        serverConnection = ReflectionUtils.invoke(minecraftServerInstance, method);

        return serverConnection;
    }
}
