package io.zabrek.soulbound.lib.logger;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Can handle thrown {@link SoulBoundException} and rate limits them so
 * they don't spam console that hard.
 */
public class CachingSoulBoundLoggerFactory implements SoulBoundLoggerFactory {

    /**
     * The list of all Loggers.
     */
    private final Map<Class<?>, Map<String, SoulBoundLogger>> loggers;

    /**
     * The decorated factory.
     */
    private final SoulBoundLoggerFactory loggerFactory;

    /**
     * Creates a new instance of a caching logger factory.
     *
     * @param loggerFactory The {@link SoulBoundLoggerFactory} to use for creating {@link SoulBoundLogger} instances.
     */
    public CachingSoulBoundLoggerFactory(final SoulBoundLoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
        this.loggers = new ConcurrentHashMap<>();
    }

    @Override
    public SoulBoundLogger create(final Class<?> clazz, @Nullable final String topic) {
        return loggers.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(nullableTopicToEmpty(topic), t -> loggerFactory.create(clazz, topic));
    }

    @Override
    public SoulBoundLogger create(final Plugin plugin, @Nullable final String topic) {
        return loggers.computeIfAbsent(plugin.getClass(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(nullableTopicToEmpty(topic), t -> loggerFactory.create(plugin, topic));
    }

    private String nullableTopicToEmpty(@Nullable final String topic) {
        return topic == null ? "" : topic;
    }
}