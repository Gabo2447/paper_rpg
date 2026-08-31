package io.zabrek.soulbound.database.factory;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.database.provider.ConnectionProvider;
import io.zabrek.soulbound.database.provider.SqliteJdbcProvider;
import io.zabrek.soulbound.database.type.Database;
import io.zabrek.soulbound.database.type.DatabaseBuilder;
import io.zabrek.soulbound.database.type.SQLite;
import org.bukkit.plugin.Plugin;

/**
 * Creates a new Sqlite Database.
 */
public class SqliteFactory implements DatabaseFactory {

    /**
     * Empty constructor for PMD.
     */
    public SqliteFactory() {
    }

    @Override
    public Database create(final ConfigAccessor config, final Plugin plugin, final SoulBoundLoggerFactory loggerFactory) {
        final ConnectionProvider sqliteProvider = new SqliteJdbcProvider(
                loggerFactory.create(SqliteJdbcProvider.class, "SQLite"),
                plugin,
                "database.db"
        );

        return DatabaseBuilder.request(SQLite.class)
                .config(config)
                .plugin(plugin)
                .logger(loggerFactory.create(SQLite.class))
                .connectionProvider(sqliteProvider)
                .build();
    }
}
