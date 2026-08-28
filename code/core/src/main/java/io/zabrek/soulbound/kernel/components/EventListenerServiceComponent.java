package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.events.DefaultEventListenerService;
import io.zabrek.soulbound.api.events.EventListenerService;
import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.kernel.DependencyProvider;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * Core component responsible for initializing and registering the {@link EventListenerService}.
 */
public class EventListenerServiceComponent implements CoreComponent {

    /**
     * Empty constructor for PMD.
     */
    public EventListenerServiceComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(JavaPlugin.class, Server.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(EventListenerService.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final JavaPlugin plugin = provider.get(JavaPlugin.class);
        final Server server = provider.get(Server.class);

        final EventListenerService eventService = new DefaultEventListenerService(plugin, server);
        provider.take(EventListenerService.class, eventService);
        plugin.getLogger().info("Registered EventListenerService for plugin " + plugin.getName());
    }
}
