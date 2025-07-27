/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.core.database;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.geysermc.databaseutils.IRepository;
import org.geysermc.databaseutils.meta.Repository;
import org.geysermc.floodgate.core.database.entity.LinkedPlayer;

@Repository
public interface PlayerLinkRepository extends IRepository<LinkedPlayer> {
    CompletableFuture<LinkedPlayer> findByBedrockIdOrJavaUniqueId(UUID bedrockId, UUID javaUniqueId);

    CompletableFuture<Boolean> existsByBedrockIdOrJavaUniqueId(UUID bedrockId, UUID javaUniqueId);

    CompletableFuture<Void> deleteByBedrockIdOrJavaUniqueId(UUID bedrockId, UUID javaUniqueId);

    CompletableFuture<Void> insert(LinkedPlayer player);
}
