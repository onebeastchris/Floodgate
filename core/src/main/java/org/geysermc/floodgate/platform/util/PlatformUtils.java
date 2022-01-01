/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
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

package org.geysermc.floodgate.platform.util;

import java.util.Collection;
import org.geysermc.floodgate.platform.command.CommandUtil;
import org.geysermc.floodgate.platform.command.TranslatableMessage;

public interface PlatformUtils {
    /**
     * Send a message to the specified player, no matter what platform Floodgate is running on.
     *
     * @param player  the player to send the message to
     * @param message the command message
     * @param locale  the locale of the player
     * @param args    the arguments
     */
    void sendMessage(Object player, String locale, TranslatableMessage message, Object... args);

    /**
     * Same as {@link CommandUtil#sendMessage(Object, String, TranslatableMessage, Object...)} except it
     * kicks the player.
     *
     * @param player  the player to send the message to
     * @param message the command message
     * @param locale  the locale of the player
     * @param args    the arguments
     */
    void kickPlayer(Object player, String locale, TranslatableMessage message, Object... args);

    Collection<String> getOnlineUsernames(PlayerType limitTo);

    enum PlayerType {
        ALL_PLAYERS,
        ONLY_BEDROCK,
        ONLY_JAVA
    }
}
