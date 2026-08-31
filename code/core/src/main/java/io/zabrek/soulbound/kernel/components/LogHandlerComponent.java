package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.config.FileConfigAccessor;
import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.kernel.DependencyProvider;
import io.zabrek.soulbound.logger.HandlerFactory;
import io.zabrek.soulbound.logger.handler.history.HistoryHandler;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.time.InstantSource;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * The implementation of {@link CoreComponent} for log handlers.
 */
public class LogHandlerComponent implements CoreComponent {

    /**
     * Create a new LogHandlerComponent.
     */
    public LogHandlerComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(Plugin.class, Server.class, BukkitScheduler.class, SoulBoundLoggerFactory.class,
                FileConfigAccessor.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(HistoryHandler.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final Plugin plugin = provider.get(Plugin.class);
        final Server server = provider.get(Server.class);
        final BukkitScheduler scheduler = provider.get(BukkitScheduler.class);
        final SoulBoundLoggerFactory loggerFactory = provider.get(SoulBoundLoggerFactory.class);
        final FileConfigAccessor config = provider.get(FileConfigAccessor.class);

        final HistoryHandler debugHistoryHandler = HandlerFactory.createHistoryHandler(loggerFactory, plugin,
                scheduler, config, new File(plugin.getDataFolder(), "/logs"), InstantSource.system());
        registerLogHandler(server, debugHistoryHandler);

        provider.take(HistoryHandler.class, debugHistoryHandler);
    }

    @SuppressWarnings("PMD.DoNotUseThreads")
    private void registerLogHandler(final Server server, final Handler handler) {
        final Logger serverLogger = server.getLogger().getParent();
        serverLogger.addHandler(handler);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            serverLogger.removeHandler(handler);
            handler.close();
        }));
    }
}
