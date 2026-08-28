package io.zabrek.soulbound.api.events;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.common.function.SoulBoundConsumer;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Default implementation of {@link EventListenerService} for managing Bukkit events.
 */
public class DefaultEventListenerService implements EventListenerService {

    /**
     * The plugin instance registering the events.
     */
    private final JavaPlugin plugin;

    /**
     * The server instance used to manage plugins and events.
     */
    private final Server server;

    /**
     * Constructs a new DefaultEventListenerService.
     *
     * @param plugin the plugin instance
     * @param server the server instance
     */
    public DefaultEventListenerService(final JavaPlugin plugin, final Server server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public <E extends Event> EventServiceSubscriptionBuilder<E> request(final Class<E> eventClass) {
        return new DefaultEventSubscriptionBuilder<>(this, eventClass);
    }

    @Override
    public <E extends Event> Subscription register(final Class<E> eventClass, final EventPriority priority, final boolean ignoredCancelled, final SoulBoundConsumer<E> handler) {
        final Listener bukkitListener = new Listener() {
        };
        server.getPluginManager().registerEvent(
                eventClass,
                bukkitListener,
                priority,
                (listener, event) -> {
                    if (eventClass.isInstance(event)) {
                        final E typedEvent = eventClass.cast(event);
                        try {
                            handler.accept(typedEvent);
                        } catch (final SoulBoundException e) {
                            getLogger().warning("Error processing the event " + eventClass.getSimpleName() + ": " + e.getMessage());
                        }
                    }
                },
                plugin,
                ignoredCancelled
        );

        return new RegisteredListenerHandle(bukkitListener);
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }
}