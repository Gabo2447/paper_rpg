package io.zabrek.soulbound.lib.logger;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

/**
 * This Factory always returns the same logger instance injected in the constructor.
 */
public class SingletonLoggerFactory implements SoulBoundLoggerFactory {

    /**
     * The logger instance to return.
     */
    private final SoulBoundLogger logger;

    /**
     * Default {@link SingletonLoggerFactory} Constructor.
     *
     * @param logger the logger instance to return
     */
    public SingletonLoggerFactory(final SoulBoundLogger logger) {
        this.logger = logger;
    }

    @Override
    public SoulBoundLogger create(final Class<?> clazz, @Nullable final String topic) {
        return logger;
    }

    @Override
    public SoulBoundLogger create(final Plugin plugin, @Nullable final String topic) {
        return logger;
    }
}