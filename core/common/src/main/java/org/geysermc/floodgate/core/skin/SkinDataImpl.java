/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.skin;

import com.google.gson.JsonObject;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;
import org.geysermc.floodgate.core.util.Constants;

public record SkinDataImpl(String value, String signature) implements SkinData {
    public static final SkinData DEFAULT_SKIN = new SkinDataImpl(
            Constants.DEFAULT_MINECRAFT_JAVA_SKIN_TEXTURE, Constants.DEFAULT_MINECRAFT_JAVA_SKIN_SIGNATURE);

    public SkinDataImpl(@NonNull String value, @MonotonicNonNull String signature) {
        this.value = Objects.requireNonNull(value);
        this.signature = Objects.requireNonNull(signature);
    }

    public static SkinData from(JsonObject data) {
        return new SkinDataImpl(
                data.get("value").getAsString(), data.get("signature").getAsString());
    }
}
