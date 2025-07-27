/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.api.link;

/**
 * @deprecated The Floodgate API has been deprecated in favor of the GeyserApi, which is shared between Geyser
 * and Floodgate
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public enum LinkRequestResult {
    /**
     * An unknown error encountered while creating / verifying the link request.
     */
    UNKNOWN_ERROR,
    /**
     * The specified bedrock username is already linked to a Java account.
     */
    ALREADY_LINKED,
    /**
     * The Bedrock player verified the request too late. The request has been expired.
     */
    REQUEST_EXPIRED,
    /**
     * The Java player hasn't requested a link to this Bedrock account.
     */
    NO_LINK_REQUESTED,
    /**
     * The entered code is invalid.
     */
    INVALID_CODE,
    /**
     * The link request has been verified successfully!
     */
    LINK_COMPLETED
}
