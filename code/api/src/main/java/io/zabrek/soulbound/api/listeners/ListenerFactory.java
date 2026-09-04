package io.zabrek.soulbound.api.listeners;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.listeners.service.ListenerService;

/**
 * Factory to create a specific {@link Listener}.
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface ListenerFactory {

    /**
     * Create a {@link Listener}.
     *
     * @param service the listener service
     * @return listener
     * @throws SoulBoundException if fails
     */
    Listener create(ListenerService service) throws SoulBoundException;
}
