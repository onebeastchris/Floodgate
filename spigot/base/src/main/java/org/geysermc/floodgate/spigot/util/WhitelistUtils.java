/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.spigot.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.geysermc.floodgate.core.util.ReflectionUtils;

@SuppressWarnings("ConstantConditions")
public final class WhitelistUtils {

    /**
     * Whitelist the given Bedrock player.
     *
     * @param uuid     the UUID of the Bedrock player to be removed
     * @param username the username of the Bedrock player to be removed
     * @param versionSpecificMethods a reference to the SpigotVersionSpecificMethods used in SpigotCommandUtil
     * @return true if the player has been removed from the whitelist, false if the player wasn't
     */
    public static boolean addPlayer(UUID uuid, String username, SpigotVersionSpecificMethods versionSpecificMethods) {
        GameProfile profile = new GameProfile(uuid, username);

        OfflinePlayer player =
                ReflectionUtils.newInstance(ClassNames.CRAFT_OFFLINE_PLAYER_CONSTRUCTOR, Bukkit.getServer(), profile);
        if (player.isWhitelisted()) {
            return false;
        }
        setWhitelist(player, true, versionSpecificMethods);
        return true;
    }

    /**
     * Removes the given Bedrock player from the whitelist.
     *
     * @param uuid     the UUID of the Bedrock player to be removed
     * @param username the username of the Bedrock player to be removed
     * @param versionSpecificMethods a reference to the SpigotVersionSpecificMethods used in SpigotCommandUtil
     * @return true if the player has been removed from the whitelist, false if the player wasn't
     * whitelisted
     */
    public static boolean removePlayer(
            UUID uuid, String username, SpigotVersionSpecificMethods versionSpecificMethods) {
        GameProfile profile = new GameProfile(uuid, username);

        OfflinePlayer player =
                ReflectionUtils.newInstance(ClassNames.CRAFT_OFFLINE_PLAYER_CONSTRUCTOR, Bukkit.getServer(), profile);
        if (!player.isWhitelisted()) {
            return false;
        }
        setWhitelist(player, false, versionSpecificMethods);
        return true;
    }

    static void setWhitelist(
            OfflinePlayer player, boolean whitelist, SpigotVersionSpecificMethods versionSpecificMethods) {
        versionSpecificMethods.maybeSchedule(() -> player.setWhitelisted(whitelist));
    }
}
