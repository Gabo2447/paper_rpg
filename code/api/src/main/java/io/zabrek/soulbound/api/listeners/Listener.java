package io.zabrek.soulbound.api.listeners;

import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.service.ListenerService;
import io.zabrek.soulbound.api.logger.SoulBoundExceptionHandler;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;

/**
 * Any listener should implement this interface.
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface Listener {

    /**
     * Should return the {@link ListenerService} for this listener.
     *
     * @return the listener service
     * @since 2.0.0
     */
    ListenerService getService();

    /**
     * Should return the listener id.
     *
     * @return the listener id
     * @since 2.0.0
     */
    default ListenerIdentifier getListenerID() {
        return getService().getListenerID();
    }

    /**
     * Should return the exception handler for this listener.
     *
     * @return the exception handler
     * @since 2.0.0
     */
    default SoulBoundExceptionHandler getExceptionHandler() {
        return getService().getExceptionHandler();
    }

    /**
     * Should return the logger for this listener.
     *
     * @return the logger
     * @since 2.0.0
     */
    default SoulBoundLogger getLogger() {
        return getService().getLogger();
    }

    /**
     * This method will be called when the listener is closed when shutting down or reloading.
     *
     * @since 2.0.0
     */
    default void close() {
        // Empty
    }
}
