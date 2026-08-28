package io.zabrek.soulbound.api.events;

import java.util.logging.Logger;

/**
 * Any listener should implement this interface.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface EventListener {

    /**
     * Should return the {@link EventListenerService} for this listener.
     *
     * @return the listener service
     * @since 1.0.0
     */
    EventListenerService getService();

    /**
     * Should return the logger for this listener.
     *
     * @return the logger
     * @since 1.0.0
     */
    default Logger getLogger() {
        return getService().getLogger();
    }

    /**
     * This method will be called when the listener is closed when shutting down or reloading.
     *
     * @since 1.0.0
     */
    default void close() {
        // Empty
    }
}