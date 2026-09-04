package io.zabrek.soulbound.api.bukkit.event;

import io.zabrek.soulbound.api.SoulBoundException;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

/**
 * A subscriber for {@link Event}s in the {@link BukkitEventService}.
 *
 * @param <T> The event type.
 * @since 2.0.0
 */
@FunctionalInterface
public interface EventServiceSubscriber<T extends Event> {

    /**
     * Called when an event of type T is fired and this subscriber is subscribed to it.
     *
     * @param event    The event.
     * @param priority The priority of the event.
     * @throws SoulBoundException If an error occurs during the execution of the subscriber.
     * @since 2.0.0
     */
    void call(T event, EventPriority priority) throws SoulBoundException;
}
