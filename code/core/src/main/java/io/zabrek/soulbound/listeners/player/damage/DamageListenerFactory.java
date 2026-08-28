package io.zabrek.soulbound.listeners.player.damage;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.events.EventListener;
import io.zabrek.soulbound.api.events.EventListenerFactory;
import io.zabrek.soulbound.api.events.EventListenerService;

/**
 * Factory responsible for creating and registering the {@link DamageListener}.
 */
public class DamageListenerFactory implements EventListenerFactory {

    /**
     * Creates a new {@link DamageListenerFactory} instances.
     */
    public DamageListenerFactory() {}

    @Override
    public EventListener create(final EventListenerService eventService) throws SoulBoundException {
        return new DamageListener(eventService);
    }

}
