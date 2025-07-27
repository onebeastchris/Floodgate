/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.standalone.codec;

import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readBool;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readIp;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readString;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readUniqueId;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.readUnsignedLong;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.writeIp;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.writeString;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.writeUniqueId;
import static org.geysermc.floodgate.core.connection.standalone.codec.CodecUtils.writeUnsignedLong;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.geysermc.api.util.BedrockPlatform;
import org.geysermc.api.util.InputMode;
import org.geysermc.api.util.UiProfile;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.connection.FloodgateConnection;
import org.geysermc.floodgate.core.connection.standalone.StandaloneFloodgateConnectionBuilder;

@Singleton
public final class FloodgateConnectionCodec {
    @Inject
    FloodgateConfig config;

    public ByteBuffer encode(FloodgateConnection connection) {
        var byteStream = new ByteArrayOutputStream();
        var stream = new DataOutputStream(byteStream);
        try {
            encode0(connection, stream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return ByteBuffer.wrap(byteStream.toByteArray());
    }

    private void encode0(FloodgateConnection connection, DataOutputStream stream) throws IOException {
        writeString(stream, connection.version());
        writeString(stream, connection.bedrockUsername());
        writeUniqueId(stream, connection.identity());
        writeUnsignedLong(stream, connection.xuid());
        stream.writeByte(connection.platform().ordinal());
        writeString(stream, connection.languageCode());
        stream.writeByte(connection.uiProfile().ordinal());
        stream.writeByte(connection.inputMode().ordinal());
        writeIp(stream, connection.ip());

        stream.writeBoolean(connection.isLinked());
        if (connection.linkedPlayer() != null) {
            LinkedPlayerCodec.encode(connection.linkedPlayer(), stream);
        }
    }

    public FloodgateConnection decode(ByteBuffer buffer) {
        var builder = new StandaloneFloodgateConnectionBuilder(config)
                .version(readString(buffer))
                .username(readString(buffer))
                .identity(readUniqueId(buffer))
                .xuid(readUnsignedLong(buffer))
                .deviceOs(BedrockPlatform.fromId(buffer.get()))
                .languageCode(readString(buffer))
                .uiProfile(UiProfile.fromId(buffer.get()))
                .inputMode(InputMode.fromId(buffer.get()))
                .ip(readIp(buffer));
        if (readBool(buffer)) {
            builder.linkedPlayer(LinkedPlayerCodec.decode(buffer));
        }

        if (buffer.hasRemaining()) {
            throw new IllegalStateException("There are still bytes (" + buffer.remaining() + ") left!");
        }

        return builder.build();
    }
}
