package io.zabrek.soulbound.database.config;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.database.Database;
import io.zabrek.soulbound.database.providers.ConnectionProvider;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

/**
 * Builder pattern implementation for instantiating {@link DatabaseConfig}.
 *
 */
public class DatabaseConfigBuilder {

    /**
     * Custom {@link SoulBoundLogger} instance.
     */
    private SoulBoundLogger logger;

    /**
     * The connection provider instance.
     */
    private ConnectionProvider connProvider;

    /**
     * The plugin instance.
     */
    private Plugin plugin;

    /**
     * The prefix for the database tables.
     */
    private String prefix;

    /**
     * Sets the plugin instance for the database.
     *
     * @param plugin the plugin instance
     * @return this builder instance for fluent chaining
     */
    public DatabaseConfigBuilder plugin(final Plugin plugin) {
        this.plugin = plugin;
        return this;
    }

    /**
     * Sets the database table prefix.
     *
     * @param prefix the table prefix string
     * @return this builder instance for fluent chaining
     */
    public DatabaseConfigBuilder prefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the custom logger instance for the database.
     *
     * @param logger the {@link SoulBoundLogger} instance
     * @return this builder instance for fluent chaining
     */
    public DatabaseConfigBuilder logger(final SoulBoundLogger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * Builds and returns {@link Database}.
     *
     * @return the instantiated database config
     */
    public <T extends Database> T build(final Class<T> dbClass) {
        final DatabaseConfig config = new DatabaseConfig(plugin, prefix, logger, connProvider);

        try {
            return dbClass.getDeclaredConstructor(DatabaseConfig.class).newInstance(config);
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException |
                       InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + dbClass.getName(), e);
        }
    }
}
