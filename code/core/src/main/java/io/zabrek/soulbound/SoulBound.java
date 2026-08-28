package io.zabrek.soulbound;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.kernel.CoreComponentLoader;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import io.zabrek.soulbound.data.PlayerDataStorage;
import io.zabrek.soulbound.database.Saver;
import io.zabrek.soulbound.faststats.FastStatsMetrics;
import io.zabrek.soulbound.kernel.SoulBoundComponents;
import io.zabrek.soulbound.kernel.TopologicalCoreComponentLoader;
import io.zabrek.soulbound.lib.logger.CachingSoulBoundLoggerFactory;
import io.zabrek.soulbound.logger.DefaultSoulBoundLoggerFactory;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Set;

/**
 * Represents SoulBound plugin.
 */
public class SoulBound extends JavaPlugin {

    /**
     * The SoulBound Plugin instance.
     */
    private static SoulBound instance;

    /**
     * The loader responsible for discovering and registering core components.
     */
    private CoreComponentLoader loader;

    /**
     * The custom logger for the plugin.
     */
    protected SoulBoundLogger log;

    /**
     * The required default constructor without arguments for plugin creation.
     */
    public SoulBound() {
        super();
    }

    /**
     * Get the plugin's instance.
     *
     * @return The plugin's instance.
     */
    public static SoulBound getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        final Set<CoreComponent> defaults = SoulBoundComponents.createDefaults(this);
        for (final CoreComponent component : defaults) {
            loader.register(component);
        }

        try {
            loader.load();
        } catch (final Exception e) {
            log.error("Failed to load SoulBound components: ", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        log.info("SoulBound plugin has been enabled.");
    }

    @Override
    public void onLoad() {
        final SoulBoundLoggerFactory loggerFactory = new CachingSoulBoundLoggerFactory(new DefaultSoulBoundLoggerFactory());
        this.log = loggerFactory.create(this);

        this.loader = new TopologicalCoreComponentLoader();
        this.loader.init(SoulBoundLoggerFactory.class, loggerFactory);
        initPluginDependencies(loader);
    }

    @Override
    public void onDisable() {

        loader.getOptional(ProfileProvider.class).map(ProfileProvider::getOnlineProfiles)
                .ifPresent(onlineProfiles -> onlineProfiles.forEach(profile -> profile.getPlayer().closeInventory()));

        loader.getOptional(Saver.class).ifPresent(Saver::end);
        loader.getOptional(FastStatsMetrics.class).ifPresent(FastStatsMetrics::disable);
        log.info("SoulBound plugin has been disabled.");
    }

    private void initPluginDependencies(final CoreComponentLoader loader) {
        loader.init(JavaPlugin.class, this);
        loader.init(Server.class, getServer());
        loader.init(PluginManager.class, getServer().getPluginManager());
        loader.init(BukkitScheduler.class, getServer().getScheduler());
        loader.init(PluginDescriptionFile.class, getDescription());
        loader.init(ServicesManager.class, getServer().getServicesManager());
    }

    /**
     * Returns the {@link CoreComponentLoader} instance.
     *
     * @return the {@link CoreComponentLoader} instance.
     */
    public CoreComponentLoader getLoader() {
        return loader;
    }

    /**
     * Returns the {@link Saver} instance used by SoulBound.
     *
     * @return the database saver
     */
    public Saver getSaver() {
        return loader.get(Saver.class);
    }

    /**
     * Gets the stored player data.
     *
     * @return storage for currently loaded player data
     */
    public PlayerDataStorage getPlayerDataStorage() {
        return loader.get(PlayerDataStorage.class);
    }
}
