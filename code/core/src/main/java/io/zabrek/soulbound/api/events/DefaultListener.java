package io.zabrek.soulbound.api.events;

import org.jetbrains.annotations.Nullable;

/**
 * Optional superclass for all objectives instead of implementing {@link EventListener} directly.
 */
public class DefaultListener implements EventListener {

    /**
     * The {@link EventListenerService} for this objective.
     */
    private final EventListenerService service;

    /**
     * The active event subscription associated with this command listener.
     */
    @Nullable
    protected Subscription subscription;

    /**
     * Creates a new instance of the objective.
     *
     * @param service the {@link EventListenerService} for this objective
     */
    public DefaultListener(final EventListenerService service) {
        this.service = service;
    }

    @Override
    public EventListenerService getService() {
        return service;
    }

    @Override
    public void close() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
