package io.zabrek.soulbound.api.listeners.handler;

import io.zabrek.soulbound.api.SoulBoundException;
import org.bukkit.event.Event;

/**
 * A handler for non-profile events.
 *
 * @param <E> the event type
 * @since 2.0.0
 */
@FunctionalInterface
public interface NonProfileListenerHandler<E extends Event> {

    /**
     * This method gets called when the related event is triggered.
     *
     * @param event the event that was triggered
     * @throws SoulBoundException when the event handling fails
     * @since 2.0.0
     */
    void handle(E event) throws SoulBoundException;
}
