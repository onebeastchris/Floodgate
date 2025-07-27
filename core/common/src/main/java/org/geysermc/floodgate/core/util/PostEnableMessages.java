/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.util;

import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.event.lifecycle.PostEnableEvent;
import org.geysermc.floodgate.core.logger.FloodgateLogger;

@Singleton
public final class PostEnableMessages {
    private final List<String> messages = new ArrayList<>();

    @Inject
    FloodgateConfig config;

    @Inject
    FloodgateLogger logger;

    @Inject
    @Named("commonScheduledPool")
    ScheduledExecutorService executorService;

    public void add(String[] message, Object... args) {
        StringBuilder builder = new StringBuilder();

        builder.append("\n**********************************\n");
        for (String part : message) {
            builder.append("* ").append(part).append('\n');
        }
        builder.append("**********************************");

        messages.add(String.format(builder.toString(), args));
    }

    @PostConstruct
    void registerPrefixMessages() {
        String prefix = config.rawUsernamePrefix();
        // todo these messages should also be translated

        if (prefix.isEmpty()) {
            add(new String[] {
                "You specified an empty prefix in your Floodgate config for Bedrock players!",
                "Should a Java player join and a Bedrock player join with the same username, unwanted results and conflicts will happen!",
                "We strongly recommend using . as the prefix, but other alternatives that will not conflict include: +, - and *"
            });
        } else if (!Utils.isUniquePrefix(prefix)) {
            add(
                    new String[] {
                        "The prefix you entered in your Floodgate config (%s) could lead to username conflicts!",
                        "Should a Java player join with the username %sNotch, and a Bedrock player join as Notch (who will be given the name %sNotch), unwanted results will happen!",
                        "We strongly recommend using . as the prefix, but other alternatives that will not conflict include: +, - and *"
                    },
                    prefix,
                    prefix,
                    prefix);
        }

        if (prefix.length() >= 16) {
            add(
                    new String[] {
                        "The prefix you entered in your Floodgate config (%s) is longer than a Java username can be!",
                        "Because of this, we reset the prefix to the default Floodgate prefix (.)"
                    },
                    prefix);
        } else if (prefix.length() > 2) {
            // we only have to warn them if we haven't replaced the prefix
            add(
                    new String[] {
                        "The prefix you entered in your Floodgate config (%s) is long! (%s characters)",
                        "A prefix is there to prevent username conflicts. However, a long prefix makes the chance of username conflicts higher.",
                        "We strongly recommend using . as the prefix, but other alternatives that will not conflict include: +, - and *"
                    },
                    prefix,
                    prefix.length());
        }
    }

    @EventListener
    public void onPostEnable(PostEnableEvent ignored) {
        // normally proxies don't have a lot of plugins, so proxies don't need to sleep as long
        executorService.schedule(() -> messages.forEach(logger::warn), config.proxy() ? 2 : 5, TimeUnit.SECONDS);
    }
}
