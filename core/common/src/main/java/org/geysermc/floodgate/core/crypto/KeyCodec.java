/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto;

import java.io.IOException;
import java.nio.file.Path;

public interface KeyCodec<S> {
    S decode(Path keyDirectory) throws IOException;

    void encode(S keys, Path keyDirectory) throws IOException;
}
