/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.event;

import io.micronaut.context.ApplicationContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.event.FireResult;
import org.geysermc.event.PostOrder;
import org.geysermc.event.bus.impl.EventBusImpl;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.event.subscribe.Subscriber;
import org.geysermc.floodgate.api.event.FloodgateEventBus;
import org.geysermc.floodgate.api.event.FloodgateSubscriber;

@Singleton
@SuppressWarnings("unchecked")
public class EventBus extends EventBusImpl<Object, FloodgateSubscriber<?>> implements FloodgateEventBus {
    @Inject
    ApplicationContext context;

    @Override
    @SuppressWarnings("rawtypes")
    public FireResult fire(@NonNull Object event) {
        context.getEventPublisher((Class) event.getClass()).publishEvent(event);
        // todo differentiate internal events from public events
        return super.fire(event);
    }

    @Override
    protected <H, T, B extends Subscriber<T>> B makeSubscription(
            @NonNull Class<T> eventClass,
            @NonNull Subscribe subscribe,
            @NonNull H listener,
            @NonNull BiConsumer<H, T> handler) {
        return (B) new EventSubscriber<>(
                eventClass, subscribe.postOrder(), subscribe.ignoreCancelled(), listener, handler);
    }

    @Override
    protected <T, B extends Subscriber<T>> B makeSubscription(
            @NonNull Class<T> eventClass, @NonNull Consumer<T> handler, @NonNull PostOrder postOrder) {
        return (B) new EventSubscriber<>(eventClass, handler, postOrder);
    }
}
