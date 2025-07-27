/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.pluginmessage;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface PluginMessageChannel {

    String getIdentifier();

    Result handleProxyCall(byte[] data, UUID sourceUuid, String sourceUsername, Identity sourceIdentity);

    Result handleServerCall(byte[] data, UUID playerUuid, String playerUsername);

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    final class Result {
        private static final Result FORWARD = new Result(true, null);
        private static final Result HANDLED = new Result(false, null);

        private final boolean allowed;
        private final String reason;

        public static Result forward() {
            return FORWARD;
        }

        public static Result handled() {
            return HANDLED;
        }

        public static Result kick(String reason) {
            return new Result(false, reason);
        }
    }

    enum Identity {
        UNKNOWN,
        SERVER,
        PLAYER
    }
}
