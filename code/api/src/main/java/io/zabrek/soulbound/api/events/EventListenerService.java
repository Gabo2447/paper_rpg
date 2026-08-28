package io.zabrek.soulbound.api.events;

import io.zabrek.soulbound.api.common.function.SoulBoundConsumer;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.logging.Logger;

/**
 * Service responsible for managing and registering event listeners dynamically.
 *
 * @since 1.0.0
 */
public interface EventListenerService {

    /**
     * Requests a builder to construct an event subscription.
     *
     * @param eventClass the class of the event
     * @param <E>        the type of the event
     * @return an event subscription builder
     * @since 1.0.0
     */
    <E extends Event> EventServiceSubscriptionBuilder<E> request(Class<E> eventClass);

    /**
     * Registers an event subscription directly with the specified parameters.
     *
     * @param eventClass       the class of the event
     * @param priority         the priority of the event listener
     * @param ignoredCancelled whether to ignore canceled events
     * @param handler          the consumer action to execute when the event fires
     * @param <E>              the type of the event
     * @return a {@link Subscription} handle to control the registration lifecycle
     * @since 1.0.0
     */
    <E extends Event> Subscription register(Class<E> eventClass, EventPriority priority, boolean ignoredCancelled, SoulBoundConsumer<E> handler);

    /**
     * Gets the logger for this listener.
     *
     * @return the logger
     * @since 1.0.0
     */
    Logger getLogger();
}