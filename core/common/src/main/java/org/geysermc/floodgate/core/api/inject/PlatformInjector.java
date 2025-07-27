/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.api.inject;

/**
 * The global interface of all the Platform Injectors. The injector can be used for various things.
 * It is used internally for getting Floodgate data out of the handshake packet and for debug mode,
 * but there is also an option to add your own addons. Note that every Floodgate platform that
 * supports netty should implement this, but the platform implementation isn't required to implement
 * this.
 */
public interface PlatformInjector<C> {
    /**
     * Injects the server connection. This will allow various addons (like getting the Floodgate
     * data and debug mode) to work.
     *
     * @throws Exception if the platform couldn't be injected
     */
    void inject() throws Exception;

    /**
     * Some platforms may not be able to remove their injection process. If so, this method will
     * return false.
     *
     * @return true if it is safe to attempt to remove our injection performed in {@link #inject()}.
     */
    default boolean canRemoveInjection() {
        return true;
    }

    /**
     * Removes the injection from the server. Please note that this function should only be used
     * internally (on plugin shutdown). This method will also remove every added addon.
     *
     * @throws Exception if the platform injection could not be removed
     */
    void removeInjection() throws Exception;

    /**
     * If the server connection is currently injected.
     *
     * @return true if the server connection is currently injected, returns false otherwise
     */
    boolean isInjected();

    /**
     * Adds an addon to the addon list of the Floodgate Injector (the addon is called when Floodgate
     * injects a channel). See {@link InjectorAddon} for more info.
     *
     * @param addon the addon to add to the addon list
     */
    void addAddon(InjectorAddon<C> addon);

    /**
     * Removes an addon from the addon list of the Floodgate Injector (the addon is called when
     * Floodgate injects a channel). See {@link InjectorAddon} for more info.
     *
     * @param addon the class of the addon to remove from the addon list
     * @param <T>   the addon type
     * @return the removed addon instance
     */
    <T extends InjectorAddon<C>> T removeAddon(Class<T> addon);
}
