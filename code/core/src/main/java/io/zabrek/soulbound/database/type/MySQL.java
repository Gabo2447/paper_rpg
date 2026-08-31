package io.zabrek.soulbound.database.type;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.database.DatabaseUpdate;
import io.zabrek.soulbound.database.MigrationKey;
import org.apache.logging.log4j.core.internal.annotation.SuppressFBWarnings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Connects to and uses a MySQL database.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class MySQL extends Database {

    /**
     * Custom {@link SoulBoundLogger} instance.
     */
    private final SoulBoundLogger log;

    /**
     * Creates a new MySQL instance.
     *
     * @param config all properties
     */
    public MySQL(final DatabaseConfig config) {
        super(config.log(), config.connProvider(), config.plugin(), config.configAccessor());
        this.log = config.log();
    }

    @Override
    protected SortedMap<MigrationKey, DatabaseUpdate> getMigrations() {
        final SortedMap<MigrationKey, DatabaseUpdate> migrations = new TreeMap<>();
        migrations.put(new MigrationKey("soulbound", 1), this::migration1);
        migrations.put(new MigrationKey("soulbound", 2), this::migration2);
        migrations.put(new MigrationKey("soulbound", 3), this::migration3);
        return migrations;
    }

    @Override
    protected Set<MigrationKey> queryExecutedMigrations(final Connection connection) throws SQLException {
        log.debug("Querying executed migrations from table '%s' migration".formatted(prefix));
        final Set<MigrationKey> executedMigrations = new HashSet<>();

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix
                    + "migration (namespace VARCHAR(63) NOT NULL, migration_id INT, "
                    + "time TIMESTAMP DEFAULT NOW(), PRIMARY KEY (namespace, migration_id))");

            try (ResultSet result = stmt.executeQuery("SELECT namespace, migration_id FROM " + prefix + "migration")) {
                while (result.next()) {
                    executedMigrations.add(new MigrationKey(result.getString("namespace"), result.getInt("migration_id")));
                }
            }
        }

        return executedMigrations;
    }

    @Override
    protected void markMigrationExecuted(final Connection connection, final MigrationKey migrationKey) throws SQLException {
        log.debug("Marking migration '%s' as executed.".formatted(migrationKey));
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + prefix + "migration (namespace, migration_id) VALUES (?, ?)")) {
            stmt.setString(1, migrationKey.namespace());
            stmt.setInt(2, migrationKey.version());
            stmt.executeUpdate();
        }
    }

    private void migration1(final Connection connection) throws SQLException {
        log.debug("Running MySQL migration 1 (initial table creation)...");
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "cooldown ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "playerID VARCHAR(256) NOT NULL, "
                    + "skill VARCHAR(256) NOT NULL, "
                    + "time TEXT NOT NULL) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "player ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "playerID VARCHAR(256) NOT NULL, "
                    + "language VARCHAR(16) NOT NULL) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "level ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "playerID VARCHAR(256) NOT NULL, "
                    + "skill VARCHAR(256) NOT NULL, "
                    + "level INT NOT NULL, "
                    + "experience INT NOT NULL) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "triggers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "playerID VARCHAR(256) NOT NULL, "
                    + "trigger VARCHAR(512) NOT NULL, "
                    + "instructions VARCHAR(2048) NOT NULL);");
        }
    }

    @SuppressFBWarnings("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
    private void migration2(final Connection connection) throws SQLException {
        log.debug("Running MySQL migration 2 (profiles table migration)...");
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "profile ("
                    + "profileID CHAR(36) NOT NULL PRIMARY KEY) ENGINE=InnoDB;");
            stmt.executeUpdate("INSERT IGNORE INTO " + prefix + "profile (profileID) "
                    + "SELECT playerID FROM " + prefix + "player;");

            stmt.executeUpdate("ALTER TABLE " + prefix + "cooldown "
                    + "CHANGE COLUMN playerID profileID CHAR(36) NOT NULL, "
                    + "MODIFY COLUMN skill VARCHAR(512) NOT NULL, "
                    + "DROP PRIMARY KEY, "
                    + "DROP COLUMN id, "
                    + "ADD PRIMARY KEY (profileID, skill), "
                    + "ADD FOREIGN KEY (profileID) REFERENCES " + prefix + "profile (profileID) ON DELETE CASCADE;");

            stmt.executeUpdate("ALTER TABLE " + prefix + "level "
                    + "MODIFY COLUMN playerID profileID CHAR(36) NOT NULL, "
                    + "MODIFY COLUMN skill VARCHAR(256) NOT NULL, "
                    + "DROP PRIMARY KEY, "
                    + "DROP COLUMN id, "
                    + "ADD PRIMARY KEY (profileID, skill), "
                    + "ADD FOREIGN KEY (profileID) REFERENCES " + prefix + "profile (profileID) ON DELETE CASCADE;");

            stmt.executeUpdate("ALTER TABLE " + prefix + "triggers "
                    + "MODIFY COLUMN playerID profileID CHAR(36) NOT NULL, "
                    + "DROP PRIMARY KEY, "
                    + "DROP COLUMN id, "
                    + "ADD PRIMARY KEY (profileID, trigger), "
                    + "ADD FOREIGN KEY (profileID) REFERENCES " + prefix + "profile (profileID) ON DELETE CASCADE;)");

            stmt.executeUpdate("ALTER TABLE " + prefix + "player "
                    + "MODIFY COLUMN playerID CHAR(36) NOT NULL, "
                    + "ADD COLUMN active_profile CHAR(36) AFTER language;");
            stmt.executeUpdate("UPDATE " + prefix + "player SET active_profile = playerID;");
            stmt.executeUpdate("ALTER TABLE " + prefix + "player "
                    + "MODIFY COLUMN active_profile CHAR(36) NOT NULL, "
                    + "DROP PRIMARY KEY, "
                    + "DROP COLUMN id, "
                    + "ADD PRIMARY KEY (playerID), "
                    + "ADD FOREIGN KEY (active_profile) REFERENCES " + prefix + "profile (profileID) ON DELETE RESTRICT;");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "player_profile ("
                    + "playerID CHAR(36) NOT NULL, "
                    + "profileID CHAR(36) NOT NULL, "
                    + "name VARCHAR(63) NULL, "
                    + "PRIMARY KEY (playerID, profileID), "
                    + "UNIQUE KEY uk_" + prefix + "player_name (playerID, name), "
                    + "FOREIGN KEY (playerID) REFERENCES " + prefix + "player (playerID) ON DELETE CASCADE, "
                    + "FOREIGN KEY (profileID) REFERENCES " + prefix + "profile (profileID) ON DELETE CASCADE) ENGINE=InnoDB;");
            stmt.executeUpdate("INSERT IGNORE INTO " + prefix + "player_profile (playerID, profileID, name) "
                    + "SELECT playerID, active_profile, NULL FROM " + prefix + "player;");
        }
    }

    @SuppressFBWarnings("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
    private void migration3(final Connection connection) throws SQLException {
        log.debug("Running MySQL migration 3 (player_profile name migration)...");
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("UPDATE " + prefix + "player_profile "
                    + "SET name = '" + profileInitialName + "' WHERE name IS NULL;");
            stmt.executeUpdate("ALTER TABLE " + prefix + "player_profile "
                    + "MODIFY COLUMN name VARCHAR(63) NOT NULL;");
        }
    }
}
