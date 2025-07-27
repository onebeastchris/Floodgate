/*
 * Copyright (c) 2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.exception;

import org.geysermc.floodgate.core.util.Constants;

public final class UnsupportedVersionException extends Exception {
    public UnsupportedVersionException(int expected, int received) {
        super(Constants.UNSUPPORTED_DATA_VERSION.formatted(expected, received));
    }
}
