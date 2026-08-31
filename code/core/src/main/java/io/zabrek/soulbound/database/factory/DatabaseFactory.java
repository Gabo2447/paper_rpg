package io.zabrek.soulbound.database.factory;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.database.type.Database;
import org.bukkit.plugin.Plugin;

/**
 * A helper class to create {@link Database}s.
 */
@FunctionalInterface
public interface DatabaseFactory {

    /**
     * Creates a new Database.
     *
     * @param config        the configuration
     * @param plugin        the plugin
     * @param loggerFactory the logger for the database
     * @return database
     */
    Database create(ConfigAccessor config, Plugin plugin, SoulBoundLoggerFactory loggerFactory);
}
