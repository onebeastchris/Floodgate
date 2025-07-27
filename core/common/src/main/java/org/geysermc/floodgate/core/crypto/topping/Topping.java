/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.topping;

import java.nio.ByteBuffer;
import java.util.List;

public interface Topping {
    ByteBuffer encode(List<ByteBuffer> dataSections);

    List<ByteBuffer> decode(ByteBuffer data);
}
