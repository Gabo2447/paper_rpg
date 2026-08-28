package io.zabrek.soulbound.api.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Factory for {@link ConfigAccessor} instances.
 *
 * @since 2.0.0
 */
public interface ConfigAccessorFactory {

    /**
     * Loads a configurationFile.
     *
     * @param configurationFile the {@link File} that is represented by this {@link FileConfigAccessor}
     * @return the created {@link FileConfigAccessor}
     * @throws InvalidConfigurationException thrown if the configurationFile could not be loaded
     * @throws FileNotFoundException         thrown if the {@code configurationFile} could not be found
     * @since 2.0.0
     */
    FileConfigAccessor create(File configurationFile) throws InvalidConfigurationException, FileNotFoundException;

    /**
     * Loads a resourceFile and save a configurationFile.
     * If the configurationFile does not exist, the resourceFile will be loaded
     * and then saved as the configurationFile if given.
     *
     * @param configurationFile the {@link File} that is represented by this {@link FileConfigAccessor}
     * @param plugin            the plugin that is the source of the resource file
     * @param resourceFile      the resource file to load from the plugin
     * @return the created {@link FileConfigAccessor}
     * @throws InvalidConfigurationException thrown if the configurationFile or the resourceFile could not be loaded,
     *                                       or the resourceFile could not be saved to the configurationFile
     * @throws FileNotFoundException         thrown if the {@code configurationFile} or the {@code resourceFile}
     *                                       could not be found
     * @since 2.0.0
     */
    FileConfigAccessor create(File configurationFile, Plugin plugin, String resourceFile) throws InvalidConfigurationException, FileNotFoundException;
}
