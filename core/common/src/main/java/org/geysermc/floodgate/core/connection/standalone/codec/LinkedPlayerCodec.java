/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.standalone.codec;

import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readString;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readUniqueId;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.writeString;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.writeUniqueId;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.geysermc.floodgate.util.LinkedPlayer;

public final class LinkedPlayerCodec {
    private LinkedPlayerCodec() {}

    public static void encode(LinkedPlayer linkedPlayer, DataOutputStream stream) throws IOException {
        writeString(stream, linkedPlayer.getJavaUsername());
        writeUniqueId(stream, linkedPlayer.getJavaUniqueId());
        writeUniqueId(stream, linkedPlayer.getBedrockId());
    }

    public static LinkedPlayer decode(ByteBuffer buffer) {
        return LinkedPlayer.of(readString(buffer), readUniqueId(buffer), readUniqueId(buffer));
    }
}
