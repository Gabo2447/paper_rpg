package io.zabrek.soulbound.database.config;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.database.providers.ConnectionProvider;
import org.bukkit.plugin.Plugin;

public record DatabaseConfig(Plugin plugin, String prefix, SoulBoundLogger logger,
                             ConnectionProvider connectionProvider) {

}
