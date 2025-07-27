/*
 * Copyright (c) 2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.audience;

import java.io.Serial;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class InvalidPlayerIdentifierException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = -6500019324607183855L;

    public InvalidPlayerIdentifierException(@NonNull String message) {
        super(message);
    }

    @Override
    public @NonNull Throwable fillInStackTrace() {
        return this;
    }
}
