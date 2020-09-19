/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 *  @author GeyserMC
 *  @link https://github.com/GeyserMC/Floodgate
 *
 */

package org.geysermc.floodgate.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.geysermc.floodgate.HandshakeHandler;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.SimpleFloodgateApi;
import org.geysermc.floodgate.api.inject.PlatformInjector;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.config.FloodgateConfig;
import org.geysermc.floodgate.config.loader.ConfigLoader;
import org.geysermc.floodgate.config.updater.ConfigFileUpdater;
import org.geysermc.floodgate.config.updater.ConfigUpdater;
import org.geysermc.floodgate.crypto.*;
import org.geysermc.floodgate.inject.CommonPlatformInjector;
import org.geysermc.floodgate.link.PlayerLinkLoader;
import org.geysermc.floodgate.util.LanguageManager;

import java.nio.file.Path;

@RequiredArgsConstructor
public final class CommonModule extends AbstractModule {
    private final Path dataDirectory;

    @Override
    protected void configure() {
        bind(FloodgateApi.class).to(SimpleFloodgateApi.class);
        bind(PlatformInjector.class).to(CommonPlatformInjector.class);
    }

    @Provides
    @Singleton
    public KeyProducer keyProducer() {
        return new AesKeyProducer();
    }

    @Provides
    @Singleton
    public FloodgateCipher cipher() {
        return new AesCipher(new Base64Topping());
    }

    @Provides
    @Singleton
    @Named("dataDirectory")
    public Path dataDirectory() {
        return dataDirectory;
    }

    @Provides
    @Singleton
    public ConfigLoader configLoader(@Named("configClass") Class<? extends FloodgateConfig> configClass,
                                     ConfigUpdater configUpdater, KeyProducer producer,
                                     FloodgateCipher cipher, FloodgateLogger logger) {
        return new ConfigLoader(
                dataDirectory, configClass, configUpdater, producer, cipher, logger
        );
    }

    @Provides
    @Singleton
    public ConfigUpdater configUpdater(ConfigFileUpdater configFileUpdater,
                                       FloodgateLogger logger) {
        return new ConfigUpdater(dataDirectory, configFileUpdater, logger);
    }

    @Provides
    @Singleton
    public LanguageManager languageLoader(FloodgateLogger logger) {
        return new LanguageManager(logger);
    }

    @Provides
    @Singleton
    public PlayerLinkLoader playerLinkLoader() {
        return new PlayerLinkLoader();
    }

    @Provides
    @Singleton
    public HandshakeHandler handshakeHandler(SimpleFloodgateApi api, FloodgateCipher cipher) {
        return new HandshakeHandler(api, cipher);
    }

    @Provides
    @Singleton
    @Named("playerAttribute")
    public AttributeKey<FloodgatePlayer> playerAttribute() {
        return AttributeKey.newInstance("floodgate-player");
    }
}
