/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.event.PostOrder;
import org.geysermc.event.subscribe.impl.SubscriberImpl;
import org.geysermc.floodgate.api.event.FloodgateSubscriber;

public final class EventSubscriber<E> extends SubscriberImpl<E> implements FloodgateSubscriber<E> {
    EventSubscriber(@NonNull Class<E> eventClass, @NonNull Consumer<E> handler, @NonNull PostOrder postOrder) {
        super(eventClass, handler, postOrder);
    }

    <H> EventSubscriber(
            Class<E> eventClass,
            PostOrder postOrder,
            boolean ignoreCancelled,
            H handlerInstance,
            BiConsumer<H, E> handler) {
        super(eventClass, postOrder, ignoreCancelled, handlerInstance, handler);
    }
}
