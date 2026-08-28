package io.zabrek.soulbound.api.events;

import io.zabrek.soulbound.api.common.function.SoulBoundConsumer;
import io.zabrek.soulbound.api.common.function.SoulBoundPredicate;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Default builder implementation for event subscriptions.
 *
 * @param <E> the type of the event being subscribed to
 * @since 1.0.0
 */
public class DefaultEventSubscriptionBuilder<E extends Event> implements EventServiceSubscriptionBuilder<E> {

    /**
     * The event service instance used to register the subscription.
     */
    private final EventListenerService eventService;

    /**
     * The class of the event to listen for.
     */
    private final Class<E> eventClass;

    /**
     * list of filters applied to validate events before handling them.
     */
    private final List<SoulBoundPredicate<E>> filters;

    /**
     * The priority of the event listener.
     */
    private EventPriority eventPriority = EventPriority.NORMAL;

    /**
     * Whether canceled events should be ignored.
     */
    private boolean isIgnoreCancelled;

    /**
     * The consumer handling the event execution.
     */
    @Nullable
    private SoulBoundConsumer<E> eventHandler;

    /**
     * Constructs a new default event subscription builder.
     *
     * @param eventService the event service
     * @param eventClass   the event class
     */
    public DefaultEventSubscriptionBuilder(final EventListenerService eventService, final Class<E> eventClass) {
        this.eventService = eventService;
        this.eventClass = eventClass;
        this.filters = new ArrayList<>();
    }

    @Override
    public EventServiceSubscriptionBuilder<E> priority(final EventPriority priority) {
        this.eventPriority = priority;
        return this;
    }

    @Override
    public EventServiceSubscriptionBuilder<E> ignoreCancelled(final boolean ignoreCancelled) {
        this.isIgnoreCancelled = ignoreCancelled;
        return this;
    }

    @Override
    public EventServiceSubscriptionBuilder<E> handler(final SoulBoundConsumer<E> handler) {
        this.eventHandler = handler;
        return this;
    }

    @Override
    public EventServiceSubscriptionBuilder<E> filter(final SoulBoundPredicate<E> filter) {
        this.filters.add(filter);
        return this;
    }

    @Override
    public Subscription build() {
        final SoulBoundConsumer<E> currentHandler = this.eventHandler;
        if (currentHandler == null) {
            throw new IllegalStateException("handler is null");
        }

        SoulBoundConsumer<E> finalHandler = currentHandler;

        if (!filters.isEmpty()) {
            final SoulBoundPredicate<E> combinedFilter = filters.stream()
                    .reduce(x -> true, SoulBoundPredicate::and);

            finalHandler = event -> {
                if (combinedFilter.test(event)) {
                    currentHandler.accept(event);
                }
            };
        }
        return eventService.register(eventClass, eventPriority, isIgnoreCancelled, finalHandler);
    }
}