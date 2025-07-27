/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class RandomUtils {
    private RandomUtils() {}

    public static SecureRandom secureRandom() {
        try {
            // use Windows-PRNG for windows (default impl is SHA1PRNG)
            if (System.getProperty("os.name").startsWith("Windows")) {
                return SecureRandom.getInstance("Windows-PRNG");
            }

            // NativePRNG (which should be the default on unix-systems) can still block your
            // system. Even though it isn't as bad as NativePRNGBlocking, we still try to
            // prevent that if possible
            return SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException ignored) {
            // at this point we just have to go with the default impl even if it blocks
            return new SecureRandom();
        }
    }
}
