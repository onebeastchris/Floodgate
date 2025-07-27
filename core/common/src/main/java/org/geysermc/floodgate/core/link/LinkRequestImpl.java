/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.link;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.geysermc.floodgate.api.link.LinkRequest;

@Getter
public final class LinkRequestImpl implements LinkRequest {
    private final String javaUsername;
    private final UUID javaUniqueId;
    private final String linkCode;
    private final String bedrockUsername;
    private final long requestTime;

    public LinkRequestImpl(String javaUsername, UUID javaUniqueId, String linkCode, String bedrockUsername) {
        this.javaUniqueId = javaUniqueId;
        this.javaUsername = javaUsername;
        this.linkCode = linkCode;
        this.bedrockUsername = bedrockUsername;
        requestTime = Instant.now().getEpochSecond();
    }

    public LinkRequestImpl(
            String javaUsername, UUID javaUniqueId, String linkCode, String bedrockUsername, long requestTime) {
        this.javaUniqueId = javaUniqueId;
        this.javaUsername = javaUsername;
        this.linkCode = linkCode;
        this.bedrockUsername = bedrockUsername;
        this.requestTime = requestTime;
    }

    @Override
    public boolean isExpired(long linkTimeout) {
        long timePassed = Instant.now().getEpochSecond() - requestTime;
        return timePassed > linkTimeout;
    }
}
