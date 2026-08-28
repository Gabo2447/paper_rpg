package io.zabrek.soulbound.api.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a configuration file that can be accessed.
 *
 * @since 2.0.0
 */
public interface ConfigAccessor extends ConfigurationSection {

    /**
     * Gets the {@link Configuration} that was loaded by this {@link ConfigAccessor}.
     *
     * @return the configuration.
     * @since 2.0.0
     */
    Configuration getConfig();
}
