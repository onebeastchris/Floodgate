/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.api.player;

import lombok.Getter;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
@Getter
public class PropertyKey {
    /**
     * Socket Address returns the InetSocketAddress of the Bedrock player
     */
    public static final PropertyKey SOCKET_ADDRESS = new PropertyKey("socket_address", false, false);

    /**
     * Skin Uploaded returns a SkinData object containing the value and signature of the Skin
     */
    public static final PropertyKey SKIN_UPLOADED = new PropertyKey("skin_uploaded", false, false);

    private final String key;
    private final boolean changeable;
    private final boolean removable;

    public PropertyKey(String key, boolean changeable, boolean removable) {
        this.key = key;
        this.changeable = changeable;
        this.removable = removable;
    }

    public Result isAddAllowed(Object obj) {
        if (obj instanceof PropertyKey) {
            PropertyKey propertyKey = (PropertyKey) obj;

            if (key.equals(propertyKey.key)) {
                if ((propertyKey.changeable == changeable || propertyKey.changeable)
                        && (propertyKey.removable == removable || propertyKey.removable)) {
                    return Result.ALLOWED;
                }
                return Result.INVALID_TAGS;
            }
            return Result.NOT_EQUALS;
        }

        if (obj instanceof String) {
            if (key.equals(obj)) {
                if (changeable) {
                    return Result.ALLOWED;
                }
                return Result.NOT_ALLOWED;
            }
            return Result.INVALID_TAGS;
        }
        return Result.NOT_EQUALS;
    }

    public enum Result {
        NOT_EQUALS,
        INVALID_TAGS,
        NOT_ALLOWED,
        ALLOWED
    }
}
