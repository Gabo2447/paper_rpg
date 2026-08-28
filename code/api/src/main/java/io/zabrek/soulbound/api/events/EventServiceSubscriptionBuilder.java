package io.zabrek.soulbound.api.events;

import io.zabrek.soulbound.api.common.function.SoulBoundConsumer;
import io.zabrek.soulbound.api.common.function.SoulBoundPredicate;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

/**
 * Builder pattern interface used to fluently configure event subscriptions.
 *
 * @param <E> the type of the event
 * @since 1.0.0
 */
public interface EventServiceSubscriptionBuilder<E extends Event> {

    /**
     * Sets the execution priority for the event listener.
     *
     * @param priority the event priority
     * @return this builder instance
     * @since 1.0.0
     */
    EventServiceSubscriptionBuilder<E> priority(EventPriority priority);

    /**
     * Configures whether the listener should ignore canceled events.
     *
     * @param ignoreCancelled true to ignore canceled events, false otherwise
     * @return this builder instance
     * @since 1.0.0
     */
    EventServiceSubscriptionBuilder<E> ignoreCancelled(boolean ignoreCancelled);

    /**
     * Sets the main action handler to execute when the event triggers.
     *
     * @param handler the consumer processing the event
     * @return this builder instance
     * @since 1.0.0
     */
    EventServiceSubscriptionBuilder<E> handler(SoulBoundConsumer<E> handler);

    /**
     * Adds a conditional filter that the event must satisfy before running the handler.
     *
     * @param filter the predicate condition
     * @return this builder instance
     * @since 1.0.0
     */
    EventServiceSubscriptionBuilder<E> filter(SoulBoundPredicate<E> filter);

    /**
     * Builds and registers the final event subscription.
     *
     * @return the created {@link Subscription} object
     * @since 1.0.0
     */
    Subscription build();
}