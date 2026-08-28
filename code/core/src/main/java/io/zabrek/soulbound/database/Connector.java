package io.zabrek.soulbound.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Connects to the database and queries it.
 */
public class Connector {

    /**
     * Logger instance for this class.
     */
    private final Logger log;

    /**
     * Table prefix.
     */
    private final String prefix;

    /**
     * Database connection management.
     */
    private final Database database;

    /**
     * Opens a new connection to the database.
     *
     * @param log      the logger for debug messages
     * @param prefix   the database table prefix
     * @param database the database to connect to
     */
    public Connector(final Logger log, final String prefix, final Database database) {
        this.log = log;
        this.prefix = prefix;
        this.database = database;
    }

    public void querySQL(final QueryType type, final Arguments args, final ResultSetCallback resulTCallback,
                         final String errorMessage) {
        final String sql = type.createSql(prefix);
        log.info("Executing SQL query type '%s' with arguments '%s' and sql: %s".formatted(type, args, sql));

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { w
            args.resolve(stmt);

        }
    }

    /**
     * Gets the database.
     *
     * @return the database used for connections
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Callback for a result set.
     */
    @FunctionalInterface
    public interface ResultSetCallback {

        /**
         * Process a result set.
         *
         * @param resultSet the result set
         * @throws SQLException if there is an error
         */
        void accept(ResultSet resultSet) throws SQLException;
    }
}
