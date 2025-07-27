/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto.topping;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class Base64Topping implements Topping {
    private static final byte SEPARATOR = 0x21;

    @Override
    public ByteBuffer encode(List<ByteBuffer> dataSections) {
        List<ByteBuffer> sections = new ArrayList<>(dataSections);

        int bufferLength = 0;
        for (int i = 0; i < sections.size(); i++) {
            var section = sections.get(i);
            var encodedSection = encodeSection(section);
            bufferLength += encodedSection.remaining();
            if (i > 0) bufferLength += 1; // Separator
            sections.set(i, encodedSection);
        }

        var buffer = ByteBuffer.allocate(bufferLength);
        for (ByteBuffer section : sections) {
            if (buffer.position() != 0) {
                buffer.put(SEPARATOR);
            }
            buffer.put(section);
        }
        // reset position after filling it
        buffer.position(0);

        return buffer;
    }

    @Override
    public List<ByteBuffer> decode(ByteBuffer data) {
        var sections = new ArrayList<ByteBuffer>();

        int previousPosition = data.position();
        while (data.hasRemaining()) {
            if (data.get() == SEPARATOR) {
                int separatorPosition = data.position() - 1;
                sections.add(decodeSection(data, previousPosition, separatorPosition));
                // don't include the separator
                previousPosition = data.position();
            }
        }
        // The remaining data is also a section
        if (data.position() - previousPosition > 0) {
            sections.add(decodeSection(data, previousPosition, data.position()));
        }

        return sections;
    }

    private ByteBuffer decodeSection(ByteBuffer buffer, int startPosition, int endPosition) {
        return Base64.getUrlDecoder().decode(buffer.slice(startPosition, endPosition - startPosition));
    }

    private ByteBuffer encodeSection(ByteBuffer section) {
        return Base64.getUrlEncoder().encode(section);
    }
}
