package io.zabrek.soulbound.database;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.database.config.DatabaseConfig;
import io.zabrek.soulbound.database.providers.ConnectionProvider;
import org.bukkit.plugin.Plugin;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 */
public abstract class Database {

    /**
     * The plugin instance, used for accessing plugin's data folder.
     */
    protected final Plugin plugin;

    /**
     * The prefix for the database tables, used to avoid conflicts with.
     */
    protected final String prefix;

    /**
     * The initial name for the profile, used when creating a new profile.
     */
    protected final String profileInitialName;

    /**
     * The connection provider instance.
     */
    protected final ConnectionProvider connectionProvider;

    /**
     * Custom {@link SoulBoundLogger} instance for this class.
     */
    private final SoulBoundLogger log;

    /**
     * Creates a new Database instance.
     *
     * @param config the config.
     */
    protected Database(final DatabaseConfig config) {
        this.log = config.logger();
        this.connectionProvider = config.connectionProvider();
        this.plugin = config.plugin();
        this.prefix = config.prefix();
        this.profileInitialName = "";
    }
}
