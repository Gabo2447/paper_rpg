package io.zabrek.soulbound.database.factory;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.database.provider.ConnectionProvider;
import io.zabrek.soulbound.database.provider.HikariProvider;
import io.zabrek.soulbound.database.provider.MySqlJdbcProvider;
import io.zabrek.soulbound.database.type.Database;
import io.zabrek.soulbound.database.type.DatabaseBuilder;
import io.zabrek.soulbound.database.type.MySQL;
import org.bukkit.plugin.Plugin;

/**
 * Creates a new MySQL database.
 */
public class MySqlFactory implements DatabaseFactory {

    /**
     * Empty constructor for PMD.
     */
    public MySqlFactory() {
    }

    @Override
    public Database create(final ConfigAccessor config, final Plugin plugin, final SoulBoundLoggerFactory loggerFactory) {
        final boolean hikariEnabled = config.getBoolean("database.hikari_pooling", true);

        final String host = config.getString("database.mysql.host", "");
        final String port = config.getString("database.mysql.port", "");
        final String base = config.getString("database.mysql.base", "");
        final String user = config.getString("database.mysql.user", "");
        final String password = config.getString("database.mysql.pass", "");

        final ConnectionProvider connProvider = hikariEnabled
                ? new HikariProvider(loggerFactory.create(HikariProvider.class, "HikariCP"), HikariProvider.HikariDriver.MYSQL, host, port, base, user, password)
                : new MySqlJdbcProvider(loggerFactory.create(MySqlJdbcProvider.class, "MySQL"), host, port, base, user, password);

        return DatabaseBuilder.request(MySQL.class)
                .connectionProvider(connProvider)
                .logger(loggerFactory.create(MySQL.class))
                .plugin(plugin)
                .config(config)
                .build();
    }
}
