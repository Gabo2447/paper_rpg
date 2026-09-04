package io.zabrek.soulbound.api;

import io.zabrek.soulbound.api.listeners.Listener;
import io.zabrek.soulbound.api.listeners.service.ListenerService;

/**
 * Optional superclass for all listener instead of implementing {@link Listener} directly.
 */
public abstract class DefaultListener implements Listener {

    /**
     * The {@link ListenerService} for this listener.
     */
    private final ListenerService service;

    /**
     * Creates a new instance of the listener.
     *
     * @param service the {@link ListenerService} for this listener.
     */
    public DefaultListener(final ListenerService service) {
        this.service = service;
    }

    @Override
    public ListenerService getService() {
        return service;
    }
}
