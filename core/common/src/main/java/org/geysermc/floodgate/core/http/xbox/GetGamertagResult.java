/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.http.xbox;

import io.avaje.jsonb.Json;
import jakarta.annotation.Nullable;

@Json
public record GetGamertagResult(@Nullable String gamertag) {}
