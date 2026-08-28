package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import io.zabrek.soulbound.kernel.DependencyProvider;
import io.zabrek.soulbound.profile.DefaultProfileProvider;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link ProfileProvider}.
 */
public class ProfileProviderComponent implements CoreComponent {

    /**
     * Create a new ProfileProviderComponent.
     */
    public ProfileProviderComponent() {
        super();
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(Plugin.class, Server.class, ServicesManager.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(ProfileProvider.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final Plugin plugin = provider.get(JavaPlugin.class);
        final Server server = provider.get(Server.class);
        final ServicesManager servicesManager = provider.get(ServicesManager.class);

        final ProfileProvider profileProvider = new DefaultProfileProvider(server);
        servicesManager.register(ProfileProvider.class, profileProvider, plugin, ServicePriority.Lowest);
        provider.take(ProfileProvider.class, servicesManager.load(ProfileProvider.class));
    }
}
