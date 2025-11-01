/*
 * Copyright (c) 2019-2025 GeyserMC. http://geysermc.org
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

package org.geysermc.floodgate.pluginmessage.channel;

import com.google.inject.Inject;
import java.util.UUID;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.config.FloodgateConfig;
import org.geysermc.floodgate.player.FloodgatePlayerImpl;
import org.geysermc.floodgate.pluginmessage.PluginMessageChannel;

public class HasFormOpenChannel implements PluginMessageChannel {
    public static final String IDENTIFIER = "floodgate:form_open";

    @Inject private FloodgateApi api;
    @Inject private FloodgateConfig config;

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Result handleProxyCall(byte[] data, UUID sourceUuid, String sourceUsername,
                                  Identity sourceIdentity) {
        if (sourceIdentity == Identity.PLAYER) {
            return handleServerCall(data, sourceUuid, sourceUsername);
        }

        if (sourceIdentity == Identity.SERVER) {
            // should never happen, only sent from Geyser
            return Result.kick("Got form open message from a server?");
        }
        return Result.handled();
    }

    @Override
    public Result handleServerCall(byte[] data, UUID playerUuid, String playerUsername) {
        FloodgatePlayer floodgatePlayer = api.getPlayer(playerUuid);
        if (floodgatePlayer == null) {
            return Result.kick("Player sent form open data for a non-Floodgate player");
        }

        FloodgatePlayerImpl player = (FloodgatePlayerImpl) floodgatePlayer;
        player.setHasFormOpen(data[0] == 1);

        return Result.handled();
    }
}
