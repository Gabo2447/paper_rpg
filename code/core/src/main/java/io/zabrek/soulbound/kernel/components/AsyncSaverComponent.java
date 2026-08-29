package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.config.ConfigAccessorFactory;
import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.database.AsyncSaver;
import io.zabrek.soulbound.database.Backup;
import io.zabrek.soulbound.database.Connector;
import io.zabrek.soulbound.kernel.DependencyProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link AsyncSaver}.
 */
@SuppressWarnings("PMD.DoNotUseThreads")
public class AsyncSaverComponent implements CoreComponent {

    /**
     * Create a new AsyncSaverComponent.
     */
    public AsyncSaverComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(JavaPlugin.class, SoulBoundLoggerFactory.class, ConfigAccessorFactory.class, ConfigAccessor.class, Connector.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(AsyncSaver.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final JavaPlugin plugin = provider.get(JavaPlugin.class);
        final SoulBoundLoggerFactory loggerFactory = provider.get(SoulBoundLoggerFactory.class);
        final ConfigAccessorFactory configAccessorFactory = provider.get(ConfigAccessorFactory.class);
        final ConfigAccessor config = provider.get(ConfigAccessor.class);
        final Connector connector = provider.get(Connector.class);

        final AsyncSaver saver = new AsyncSaver(loggerFactory.create(AsyncSaver.class, "AsyncSaver"),
                config.getLong("database.reconnect_interval"), connector);
        new Thread(saver).start();
        new Backup(loggerFactory, loggerFactory.create(Backup.class), configAccessorFactory, plugin.getDataFolder(), connector)
                .loadDatabaseFromBackup();

        provider.take(AsyncSaver.class, saver);
    }
}
