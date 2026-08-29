package io.zabrek.soulbound.database.provider;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a Sqlite JDBC connection.
 */
public class SqliteJdbcProvider implements ConnectionProvider {

    /**
     * The logger instances.
     */
    private final SoulBoundLogger log;

    /**
     * The plugin instance.
     */
    private final Plugin plugin;

    /**
     * The location of the database file.
     */
    private final String dbLocation;

    /**
     * Creates a new SQLite JDBC provider.
     *
     * @param log        the logger instance
     * @param plugin     the plugin instance
     * @param dbLocation the location of the database file
     */
    public SqliteJdbcProvider(final SoulBoundLogger log, final Plugin plugin, final String dbLocation) {
        this.log = log;
        this.plugin = plugin;
        this.dbLocation = dbLocation;
    }

    @Override
    public Connection create() {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
            log.error("unable to create plugin data folder!");
        }

        final File file = new File(plugin.getDataFolder(), dbLocation);
        if (!file.exists()) {
            log.debug("SQLite database file does not exist, creating new file '%s'".formatted(file.getPath()));
            try {
                if (file.createNewFile()) {
                    log.debug("Created SQLite database file '%s'".formatted(file.getPath()));
                } else {
                    log.error("Unable to create SQLite database '%s'!".formatted(file.getPath()));
                }
            } catch (final IOException e) {
                log.error("Unable to create database!", e);
            }
        }

        Connection conn = null;
        try {
            final String jdbcPath = "jdbc:sqlite:%s/%s".formatted(plugin.getDataFolder().toPath(), dbLocation);

            log.debug("Checking for SQLite JDBC driver...");
            Class.forName("org.sqlite.JDBC");

            log.debug("Connecting via SQLite JDBC to '%s'".formatted(jdbcPath));
            conn = DriverManager.getConnection(jdbcPath);
            log.debug("SQLite JDBC connection established successfully.");
        } catch (final ClassNotFoundException | SQLException e) {
            log.error("There was an exception with creating the Sqlite connection.", e);
        }

        if (conn == null) {
            throw new IllegalStateException("Not able to create a database connection!");
        }
        return conn;
    }
}
