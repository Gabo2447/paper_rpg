package io.zabrek.soulbound.lib.config;

import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.config.ConfigAccessorFactory;
import io.zabrek.soulbound.api.config.FileConfigAccessor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Factory for {@link ConfigAccessor} instances.
 */
public class DefaultConfigAccessorFactory implements ConfigAccessorFactory {

    /**
     * Creates a new DefaultConfigAccessorFactory instance.
     */
    public DefaultConfigAccessorFactory() {
    }

    @Override
    public FileConfigAccessor create(final File configurationFile) throws InvalidConfigurationException, FileNotFoundException {
        return create(configurationFile, null, null);
    }

    @Override
    public FileConfigAccessor create(@Nullable final File configurationFile, @Nullable final Plugin plugin, @Nullable final String resourceFile) throws InvalidConfigurationException, FileNotFoundException {
        return new StandardConfigAccessor(configurationFile, plugin, resourceFile);
    }
}
