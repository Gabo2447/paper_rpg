package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.events.EventListener;
import io.zabrek.soulbound.api.events.EventListenerFactory;
import io.zabrek.soulbound.api.events.EventListenerService;
import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.kernel.DependencyProvider;
import io.zabrek.soulbound.listeners.player.join.JoinListenerFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Register and load the all listeners.
 */
public class ListenerComponent implements CoreComponent {

    /**
     * Instances a new ListenerComponent.
     */
    public ListenerComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(JavaPlugin.class, EventListenerService.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of();
    }

    @Override
    public void load(final DependencyProvider provider) {
        final JavaPlugin plugin = provider.get(JavaPlugin.class);
        final EventListenerService eventService = provider.get(EventListenerService.class);

        final Set<EventListenerFactory> factories = Set.of(
                new JoinListenerFactory()
        );

        final Set<EventListener> loadedListeners = new HashSet<>();

        try {
            for (final EventListenerFactory factory : factories) {
                final EventListener listener = factory.create(eventService);
                loadedListeners.add(listener);
            }
        } catch (final SoulBoundException e) {
            plugin.getLogger().warning("Failed to load listeners: " + e.getMessage());
        }

        plugin.getLogger().info("Loaded " + loadedListeners.size() + " event listeners for plugin " + plugin.getName());
    }
}
