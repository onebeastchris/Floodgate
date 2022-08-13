/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Floodgate
 */

package org.geysermc.floodgate.event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.geysermc.event.bus.impl.EventBusImpl;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.event.subscribe.Subscriber;

@SuppressWarnings("unchecked")
public final class EventBus extends EventBusImpl<Object, EventSubscriber<?>> {
    @Override
    protected <H, T, B extends Subscriber<T>> B makeSubscription(
            Class<T> eventClass,
            Subscribe subscribe,
            H listener,
            BiConsumer<H, T> handler) {
        return (B) new EventSubscriber<>(
                eventClass, subscribe.postOrder(), subscribe.ignoreCancelled(), listener, handler
        );
    }

    @Override
    protected <T, B extends Subscriber<T>> B makeSubscription(
            Class<T> eventClass,
            Consumer<T> handler) {
        return (B) new EventSubscriber<>(eventClass, handler);
    }
}
