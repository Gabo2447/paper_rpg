package io.zabrek.soulbound.logger;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating {@link SoulBoundLogger} instances.
 */
public class DefaultSoulBoundLoggerFactory implements SoulBoundLoggerFactory {

    /**
     * Creates a new instance.
     */
    public DefaultSoulBoundLoggerFactory() {
    }

    @Override
    @SuppressWarnings("PMD.UseProperClassLoader")
    public SoulBoundLogger create(final Class<?> clazz, @Nullable final String topic) {
        if (Plugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("It is not allowed to use this create method from the class '"
                    + clazz.getName() + "' which directly or indirectly extends 'org.bukkit.plugin.Plugin'!");
        }
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getClass().getClassLoader().equals(clazz.getClassLoader())) {
                return new DefaultSoulBoundLogger(plugin, plugin.getLogger(), clazz, topic);
            }
        }
        throw new IllegalStateException("The class '" + clazz.getName()
                + "' has not been loaded by a 'org.bukkit.plugin.Plugin'. "
                + "Therefore, it was not possible to create a logger for this class!");
    }

    @Override
    public SoulBoundLogger create(final Plugin plugin, @Nullable final String topic) {
        return new DefaultSoulBoundLogger(plugin, plugin.getLogger(), plugin.getClass(), topic);
    }
}
