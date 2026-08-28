package io.zabrek.soulbound.api.events;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * Factory to create a specific {@link EventListener}.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface EventListenerFactory {

    /**
     * Create a {@link EventListener}.
     *
     * @param eventService the listener service
     * @return the listener
     * @throws SoulBoundException if fails
     * @since 1.0.0
     */
    EventListener create(EventListenerService eventService) throws SoulBoundException;
}