package io.zabrek.soulbound.kernel.components;

import com.zaxxer.hikari.pool.HikariPool;
import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.database.Connector;
import io.zabrek.soulbound.database.provider.ConnectionProvider;
import io.zabrek.soulbound.database.provider.HikariProvider;
import io.zabrek.soulbound.database.provider.MySqlJdbcProvider;
import io.zabrek.soulbound.database.provider.SqliteJdbcProvider;
import io.zabrek.soulbound.database.type.Database;
import io.zabrek.soulbound.database.type.DatabaseBuilder;
import io.zabrek.soulbound.database.type.DatabaseType;
import io.zabrek.soulbound.database.type.MySQL;
import io.zabrek.soulbound.database.type.SQLite;
import io.zabrek.soulbound.kernel.DependencyProvider;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link Connector}.
 */
public class DatabaseComponent implements CoreComponent {

    /**
     * Whether the database is using MySQL.
     */
    private boolean mySql;

    /**
     * Create a new DatabaseComponent.
     */
    public DatabaseComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(JavaPlugin.class, SoulBoundLoggerFactory.class, ConfigAccessor.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(DatabaseComponent.class, Connector.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final SoulBoundLoggerFactory loggerFactory = provider.get(SoulBoundLoggerFactory.class);
        final ConfigAccessor config = provider.get(ConfigAccessor.class);
        final JavaPlugin plugin = provider.get(JavaPlugin.class);

        final SoulBoundLogger log = loggerFactory.create(DatabaseComponent.class);

        final boolean hikariEnabled = config.getBoolean("database.hikari_pooling", true);
        final DatabaseType databaseType = DatabaseType.fromString(config.getString("database.type", "sqlite"));

        Database database = null;
        if (databaseType == DatabaseType.MYSQL) {
            log.debug("Connecting to MySQL database...");
            database = tryConnectMySQL(config, hikariEnabled, plugin, loggerFactory, log);
        }

        if (database == null) {
            if (databaseType == DatabaseType.MYSQL) {
                log.warn("No connection to the MySQL database! Using SQLite for storing data as fallback!");
            } else {
                log.info("Using SQLite for storing data!");
            }
            database = createSqliteDatabase(config, plugin, loggerFactory);
        }

        database.createTables();
        final Connector connector = new Connector(
                loggerFactory.create(Connector.class, "DatabaseConnector"),
                config.getString("database.prefix", "sb_"),
                database);

        provider.take(Connector.class, connector);
        provider.take(DatabaseComponent.class, this);
    }

    /**
     * Whether the database is using MySQL.
     *
     * @return if the database is using MySQL
     */
    public boolean usesMySQL() {
        return mySql;
    }

    @Nullable
    private Database tryConnectMySQL(final ConfigAccessor config, final boolean hikariEnabled, final Plugin plugin,
                                     final SoulBoundLoggerFactory loggerFactory, final SoulBoundLogger log) {
        final String host = config.getString("database.mysql.host", "");
        final String port = config.getString("database.mysql.port", "");
        final String base = config.getString("database.mysql.base", "");
        final String user = config.getString("database.mysql.user", "");
        final String password = config.getString("database.mysql.pass", "");

        if (user.isBlank() || base.isBlank()) {
            log.warn("MySQL configuration is incomplete (user or database is empty).");
            return null;
        }

        try {
            final ConnectionProvider connProvider = hikariEnabled
                    ? new HikariProvider(loggerFactory.create(HikariProvider.class, "HikariCP"), HikariProvider.HikariDriver.MYSQL, host, port, base, user, password)
                    : new MySqlJdbcProvider(loggerFactory.create(MySqlJdbcProvider.class, "MySQL"), host, port, base, user, password);

            final Database mySQL = DatabaseBuilder.request(MySQL.class)
                    .logger(loggerFactory.create(MySQL.class))
                    .connectionProvider(connProvider)
                    .plugin(plugin)
                    .config(config)
                    .build();

            try (Connection conn = mySQL.getConnection()) {
                if (conn.isValid(5000)) {
                    this.mySql = true;
                    log.info("Successfully connected to MySQL database!");
                    return mySQL;
                }
            }
        } catch (final SQLException | HikariPool.PoolInitializationException e) {
            log.warn("Failed to connect to MySQL database: %s".formatted(e.getMessage()));
            log.debug("MySQL connection error details:", e);
        }
        return null;
    }

    private Database createSqliteDatabase(final ConfigAccessor config, final Plugin plugin, final SoulBoundLoggerFactory loggerFactory) {
        final ConnectionProvider sqliteProvider = new SqliteJdbcProvider(
                loggerFactory.create(SqliteJdbcProvider.class, "SQLite"),
                plugin,
                "database.db"
        );
        return DatabaseBuilder.request(SQLite.class)
                .logger(loggerFactory.create(SQLite.class))
                .connectionProvider(sqliteProvider)
                .plugin(plugin)
                .config(config)
                .build();
    }
}
