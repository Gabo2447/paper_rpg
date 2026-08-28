package io.zabrek.soulbound;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.kernel.CoreComponentLoader;
import io.zabrek.soulbound.kernel.SoulBoundComponents;
import io.zabrek.soulbound.kernel.TopologicalCoreComponentLoader;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents SoulBound plugin.
 */
public class SoulBound extends JavaPlugin {

    /**
     * The SoulBound Plugin instance.
     */
    private static SoulBound instance;

    /**
     * The logger instance.
     */
    protected Logger log;

    /**
     * The loader responsible for discovering and registering core components.
     */
    private CoreComponentLoader loader;

    /**
     * The required default constructor without arguments for plugin creation.
     */
    public SoulBound() {
        super();
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
            log.severe("Failed to load SoulBound components: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        log.info("SoulBound has been enabled.");
    }

    @Override
    public void onLoad() {
        this.log = getLogger();

        this.loader = new TopologicalCoreComponentLoader();
        initPluginDependencies(loader);
    }

    @Override
    public void onDisable() {
        log.info("SoulBound has been disabled.");
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
     * Get the plugin's instance.
     *
     * @return The plugin's instance.
     */
    public static SoulBound getInstance() {
        return instance;
    }

    /**
     * Returns the {@link CoreComponentLoader} instance.
     *
     * @return the {@link CoreComponentLoader} instance.
     */
    public CoreComponentLoader getLoader() {
        return loader;
    }
}
