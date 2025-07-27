/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BedrockData implements Cloneable {
    public static final int EXPECTED_LENGTH = 12;

    private final String version;
    private final String username;
    private final String xuid;
    private final int deviceOs;
    private final String languageCode;
    private final int uiProfile;
    private final int inputMode;
    private final String ip;
    private final LinkedPlayer linkedPlayer;
    private final boolean fromProxy;

    private final int subscribeId;
    private final String verifyCode;

    private final int dataLength;

    public static BedrockData of(
            String version,
            String username,
            String xuid,
            int deviceOs,
            String languageCode,
            int uiProfile,
            int inputMode,
            String ip,
            LinkedPlayer linkedPlayer,
            boolean fromProxy,
            int subscribeId,
            String verifyCode) {
        return new BedrockData(
                version,
                username,
                xuid,
                deviceOs,
                languageCode,
                inputMode,
                uiProfile,
                ip,
                linkedPlayer,
                fromProxy,
                subscribeId,
                verifyCode,
                EXPECTED_LENGTH);
    }

    public static BedrockData of(
            String version,
            String username,
            String xuid,
            int deviceOs,
            String languageCode,
            int uiProfile,
            int inputMode,
            String ip,
            int subscribeId,
            String verifyCode) {
        return of(
                version,
                username,
                xuid,
                deviceOs,
                languageCode,
                uiProfile,
                inputMode,
                ip,
                null,
                false,
                subscribeId,
                verifyCode);
    }

    public static BedrockData fromString(String data) {
        String[] split = data.split("\0");
        if (split.length != EXPECTED_LENGTH) {
            return emptyData(split.length);
        }

        LinkedPlayer linkedPlayer = LinkedPlayer.fromString(split[8]);
        // The format is the same as the order of the fields in this class
        return new BedrockData(
                split[0],
                split[1],
                split[2],
                Integer.parseInt(split[3]),
                split[4],
                Integer.parseInt(split[5]),
                Integer.parseInt(split[6]),
                split[7],
                linkedPlayer,
                "1".equals(split[9]),
                Integer.parseInt(split[10]),
                split[11],
                split.length);
    }

    private static BedrockData emptyData(int dataLength) {
        return new BedrockData(null, null, null, -1, null, -1, -1, null, null, false, -1, null, dataLength);
    }

    public boolean hasPlayerLink() {
        return linkedPlayer != null;
    }

    @Override
    public String toString() {
        // The format is the same as the order of the fields in this class
        return version
                + '\0'
                + username
                + '\0'
                + xuid
                + '\0'
                + deviceOs
                + '\0'
                + languageCode
                + '\0'
                + uiProfile
                + '\0'
                + inputMode
                + '\0'
                + ip
                + '\0'
                + (linkedPlayer != null ? linkedPlayer.toString() : "null")
                + '\0'
                + (fromProxy ? 1 : 0)
                + '\0'
                + subscribeId
                + '\0'
                + verifyCode;
    }

    @Override
    public BedrockData clone() throws CloneNotSupportedException {
        return (BedrockData) super.clone();
    }
}
