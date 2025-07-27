/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.platform.listener;

/**
 * This class is responsible for registering listeners to the listener manager of the platform that
 * is currently in use. Unfortunately due to the major differences between the platforms (when it
 * comes to listeners) every Floodgate platform has to implement their own listeners.
 *
 * @param <T> the platform-specific listener class
 */
public interface ListenerRegistration<T> {
    /**
     * This method will register the specified listener.
     *
     * @param listener the listener to register
     */
    void register(T listener);
}
