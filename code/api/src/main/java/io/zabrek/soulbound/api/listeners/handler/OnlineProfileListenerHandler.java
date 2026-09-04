package io.zabrek.soulbound.api.listeners.handler;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.profile.OnlineProfile;
import org.bukkit.event.Event;

/**
 * A handler for events with profiles involved.
 *
 * @param <E> the event type
 * @since 2.0.0
 */
@FunctionalInterface
public interface OnlineProfileListenerHandler<E extends Event> {

    /**
     * This method gets called when the related event is triggered.
     *
     * @param event   the event that was triggered
     * @param profile the event-related profile extracted from the event
     * @throws SoulBoundException when the event handling fails
     * @since 2.0.0
     */
    void handle(E event, OnlineProfile profile) throws SoulBoundException;
}
