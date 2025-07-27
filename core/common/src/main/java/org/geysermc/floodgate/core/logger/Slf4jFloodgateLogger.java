/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.logger;

import io.micronaut.context.BeanProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.geysermc.floodgate.core.config.FloodgateConfig;
import org.geysermc.floodgate.core.platform.command.Placeholder;
import org.geysermc.floodgate.core.platform.command.TranslatableMessage;
import org.geysermc.floodgate.core.util.LanguageManager;
import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

@Singleton
public final class Slf4jFloodgateLogger implements FloodgateLogger {
    @Inject
    BeanProvider<LanguageManager> languageManager;

    @Inject
    Logger logger;

    @Inject
    void init(FloodgateConfig config) {
        //        if (config.debug() && !logger.isDebugEnabled()) {
        //            Configurator.setLevel(logger.getName(), Level.DEBUG);
        //        }
    }

    @Override
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    @Override
    public void error(String message, Throwable throwable, Object... args) {
        logger.error(MessageFormatter.basicArrayFormat(message, args), throwable);
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void info(Component message) {
        // todo check on other platforms if just serializing the component works.
        // it does on Velocity
        logger.info(MiniMessage.miniMessage().serialize(message));
    }

    @Override
    public void translatedInfo(TranslatableMessage message, Placeholder... args) {
        var manager = languageManager.get();
        info(message.translateMessage(manager, manager.getDefaultLocale(), args));
    }

    @Override
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    @Override
    public void trace(String message, Object... args) {
        logger.trace(message, args);
    }

    @Override
    public boolean isDebug() {
        return logger.isDebugEnabled();
    }
}
