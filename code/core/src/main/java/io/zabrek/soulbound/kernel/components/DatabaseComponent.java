package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.database.Connector;
import io.zabrek.soulbound.database.factory.DatabaseFactory;
import io.zabrek.soulbound.database.factory.MySqlFactory;
import io.zabrek.soulbound.database.factory.SqliteFactory;
import io.zabrek.soulbound.database.type.Database;
import io.zabrek.soulbound.database.type.DatabaseType;
import io.zabrek.soulbound.kernel.DependencyProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link Connector}.
 */
public class DatabaseComponent implements CoreComponent {

    /**
     * A map holding the registered database factories.
     */
    private final Map<DatabaseType, DatabaseFactory> factories = new EnumMap<>(DatabaseType.class);

    /**
     * Create a new DatabaseComponent.
     */
    public DatabaseComponent() {
        factories.put(DatabaseType.MYSQL, new MySqlFactory());
        factories.put(DatabaseType.SQLITE, new SqliteFactory());
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

        final DatabaseType databaseType = DatabaseType.fromString(config.getString("database.type", "sqlite"));
        final DatabaseFactory factory = factories.get(databaseType);

        if (factory == null) {
            throw new IllegalStateException("Unknown database type: %s".formatted(databaseType));
        }

        log.info("Using '%s' database backend.".formatted(databaseType));
        final Database database = factory.create(config, plugin, loggerFactory);

        database.createTables();
        final Connector connector = new Connector(
                loggerFactory.create(Connector.class, "DatabaseConnector"),
                config.getString("database.prefix", "sb_"),
                database
        );

        provider.take(Connector.class, connector);
        provider.take(DatabaseComponent.class, this);
    }
}
