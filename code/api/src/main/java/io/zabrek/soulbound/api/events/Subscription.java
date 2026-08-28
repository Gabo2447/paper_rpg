package io.zabrek.soulbound.api.events;

/**
 * Represents an active event subscription that can be managed or canceled.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface Subscription {

    /**
     * Unsubscribes or unregisters the listener, stopping it from receiving further events.
     *
     * @since 1.0.0
     */
    void unsubscribe();
}