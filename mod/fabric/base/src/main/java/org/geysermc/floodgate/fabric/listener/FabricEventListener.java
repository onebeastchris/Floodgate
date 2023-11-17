/*
 * Copyright (c) 2019-2023 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Floodgate
 */

package org.geysermc.floodgate.fabric.listener;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.geysermc.event.Listener;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.core.api.SimpleFloodgateApi;
import org.geysermc.floodgate.core.connection.ConnectionManager;
import org.geysermc.floodgate.core.listener.McListener;
import org.geysermc.floodgate.core.util.LanguageManager;

import java.lang.annotation.Annotation;

@Singleton
public final class FabricEventListener implements McListener {

    @Inject ConnectionManager connectionManager;
    @Inject private SimpleFloodgateApi api;
    @Inject private LanguageManager languageManager;
    @Inject private FloodgateLogger logger;

    public void onPlayerJoin(ServerGamePacketListenerImpl networkHandler, PacketSender packetSender, MinecraftServer server) {
        var connection = connectionManager.findPendingConnection(networkHandler.player.getUUID());
        if (connection == null) {
            return;
        }

        languageManager.loadLocale(connection.languageCode());
        connectionManager.addAcceptedConnection(connection);
    }

    public void onPlayerDisconnect(ServerGamePacketListenerImpl listener, MinecraftServer server) {
        connectionManager.removeConnection(listener.player);
    }
}