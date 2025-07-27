/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.connection.standalone.codec;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

final class CodecUtils {
    private CodecUtils() {}

    public static boolean readBool(ByteBuffer buffer) {
        return buffer.get() == 1;
    }

    public static String readString(ByteBuffer buffer) {
        var bytes = new byte[readVarInt(buffer)];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeString(DataOutputStream stream, String value) throws IOException {
        var bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(stream, bytes.length);
        stream.write(bytes);
    }

    public static String readUnsignedLong(ByteBuffer buffer) {
        return Long.toUnsignedString(buffer.getLong());
    }

    public static void writeUnsignedLong(DataOutputStream stream, String value) throws IOException {
        stream.writeLong(Long.parseUnsignedLong(value));
    }

    public static int readVarInt(ByteBuffer buffer) {
        int value = 0;
        int size = 0;
        int b;
        while (((b = buffer.get()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << (size++ * 7);
            if (size > 5) {
                throw new IllegalArgumentException("VarInt too long (length must be <= 5)");
            }
        }

        return value | ((b & 0x7F) << (size * 7));
    }

    public static void writeVarInt(DataOutputStream stream, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            stream.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }

        stream.writeByte(value);
    }

    public static UUID readUniqueId(ByteBuffer buffer) {
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public static void writeUniqueId(DataOutputStream stream, UUID value) throws IOException {
        stream.writeLong(value.getMostSignificantBits());
        stream.writeLong(value.getLeastSignificantBits());
    }

    public static InetAddress readIp(ByteBuffer buffer) {
        var address = new byte[buffer.get()];
        buffer.get(address);
        try {
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void writeIp(DataOutputStream stream, InetAddress ip) throws IOException {
        var address = ip.getAddress();
        stream.writeByte(address.length);
        stream.write(address);
    }
}
