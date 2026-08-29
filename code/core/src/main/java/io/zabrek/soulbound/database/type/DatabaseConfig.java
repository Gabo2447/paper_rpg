package io.zabrek.soulbound.database.type;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.database.provider.ConnectionProvider;
import org.bukkit.plugin.Plugin;

/**
 * Configuration container for database initialization.
 *
 * @param log            the logger instance
 * @param connProvider   the connection provider
 * @param plugin         the plugin instance
 * @param configAccessor the config accessor
 */
public record DatabaseConfig(SoulBoundLogger log, ConnectionProvider connProvider, Plugin plugin,
                             ConfigAccessor configAccessor) {

}
