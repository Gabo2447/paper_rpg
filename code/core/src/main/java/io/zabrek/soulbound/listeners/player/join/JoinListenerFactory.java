package io.zabrek.soulbound.listeners.player.join;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.events.EventListener;
import io.zabrek.soulbound.api.events.EventListenerFactory;
import io.zabrek.soulbound.api.events.EventListenerService;

/**
 * Factory responsible for creating and registering the {@link JoinListener}.
 */
public class JoinListenerFactory implements EventListenerFactory {

    /**
     * Creates a new {@link JoinListenerFactory} instances.
     */
    public JoinListenerFactory() {
    }

    @Override
    public EventListener create(final EventListenerService eventService) throws SoulBoundException {
        return new JoinListener(eventService);
    }
}
