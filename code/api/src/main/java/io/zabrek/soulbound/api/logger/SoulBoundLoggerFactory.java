package io.zabrek.soulbound.api.logger;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for {@link SoulBoundLogger} instances.
 *
 * @since 2.0.0
 */
public interface SoulBoundLoggerFactory {

    /**
     * Creates a logger for a given class.
     * <p>
     * Use this method to create a logger without a topic.
     *
     * @param clazz The class to create a logger for.
     * @return A {@link SoulBoundLogger} implementation.
     * @throws IllegalStateException Thrown if this is called from a class, that extends {@link Plugin}
     * @since 2.0.0
     */
    default SoulBoundLogger create(final Class<?> clazz) {
        return create(clazz, null);
    }

    /**
     * Creates a logger for a given class.
     * <p>
     * Use this method to create a logger with a topic.
     *
     * @param clazz The class to create a logger for.
     * @param topic The optional topic of the logger.
     * @return A {@link SoulBoundLogger} implementation.
     * @throws IllegalStateException Thrown if this is called from a class, that extends {@link Plugin}
     * @since 2.0.0
     */
    SoulBoundLogger create(Class<?> clazz, @Nullable String topic);

    /**
     * Creates a logger.
     * <p>
     * Use this method to create a logger for the {@link Plugin} class without a topic.
     * For other classes use the {@link SoulBoundLoggerFactory#create(Class)}
     * or {@link SoulBoundLoggerFactory#create(Class, String)} method.
     *
     * @param plugin The plugin which is used for logging.
     * @return A {@link SoulBoundLogger} implementation.
     * @since 2.0.0
     */
    default SoulBoundLogger create(final Plugin plugin) {
        return create(plugin, null);
    }

    /**
     * Creates a logger.
     * <p>
     * Use this method to create a logger for the {@link Plugin} class without a topic.
     * For other classes use the {@link SoulBoundLoggerFactory#create(Class)}
     * or {@link SoulBoundLoggerFactory#create(Class, String)} method.
     *
     * @param plugin The plugin which is used for logging.
     * @param topic  The optional topic of the logger.
     * @return A {@link SoulBoundLogger} implementation.
     * @since 2.0.0
     */
    SoulBoundLogger create(Plugin plugin, @Nullable String topic);
}
