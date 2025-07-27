/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.command.util;

public enum Permission {
    COMMAND_MAIN("floodgate.command.floodgate", PermissionDefault.TRUE),
    COMMAND_MAIN_FIREWALL(COMMAND_MAIN, "firewall", PermissionDefault.OP),
    COMMAND_MAIN_VERSION(COMMAND_MAIN, "version", PermissionDefault.OP),
    COMMAND_LINK("floodgate.command.linkaccount", PermissionDefault.TRUE),
    COMMAND_UNLINK("floodgate.command.unlinkaccount", PermissionDefault.TRUE),
    COMMAND_WHITELIST("floodgate.command.fwhitelist", PermissionDefault.OP),
    COMMAND_LINKED("floodgate.command.linkedaccounts", PermissionDefault.OP),
    COMMAND_LINKED_MANAGE(COMMAND_LINKED, "manage", PermissionDefault.OP),

    NEWS_RECEIVE("floodgate.news.receive", PermissionDefault.OP);

    private final String permission;
    private final PermissionDefault defaultValue;

    Permission(String permission, PermissionDefault defaultValue) {
        this.permission = permission;
        this.defaultValue = defaultValue;
    }

    Permission(Permission parent, String child, PermissionDefault defaultValue) {
        this(parent.get() + "." + child, defaultValue);
    }

    public String get() {
        return permission;
    }

    public PermissionDefault defaultValue() {
        return defaultValue;
    }
}
