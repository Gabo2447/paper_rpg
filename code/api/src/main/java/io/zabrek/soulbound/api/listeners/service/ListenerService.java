package io.zabrek.soulbound.api.listeners.service;

import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.ListenerFactory;
import io.zabrek.soulbound.api.logger.SoulBoundExceptionHandler;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import org.bukkit.event.Event;

/**
 * Provides services for listener creation and event subscriptions.
 *
 * @since 2.0.0
 */
public interface ListenerService {

    /**
     * Requests a new event subscription using an {@link ListenerServiceSubscriptionBuilder}.
     * <br>
     * Calling this in the context of an {@link ListenerFactory} will cause
     * {@link ListenerServiceSubscriptionBuilder#source(ListenerIdentifier)} to be called
     * with the listener's source before returning.
     * <br>
     * The request may be completed in one chain of calls requiring at least a handler and ending with
     * {@link ListenerServiceSubscriptionBuilder#subscribe(boolean)}.
     *
     * @param eventClass the event class to subscribe to
     * @param <T>        the event type
     * @return a new {@link ListenerServiceSubscriptionBuilder} for the requested event
     * @since 2.0.0
     */
    <T extends Event> ListenerServiceSubscriptionBuilder<T> request(Class<T> eventClass);

    /**
     * Get an exception handler specifically for this listener.
     *
     * @return the exception handler
     * @since 2.0.0
     */
    SoulBoundExceptionHandler getExceptionHandler();

    /**
     * Get the logger specifically for this listener.
     *
     * @return the logger
     * @since 2.0.0
     */
    SoulBoundLogger getLogger();

    /**
     * Get the general profile provider.
     *
     * @return the profile provider
     * @since 2.0.0
     */
    ProfileProvider getProfileProvider();

    /**
     * Get the data provider.
     *
     * @return the data service
     * @since 2.0.0
     */
    ListenerDataService getData();

    /**
     * Retrieves the listener ID.
     *
     * @return the listener ID
     * @since 2.0.0
     */
    ListenerIdentifier getListenerID();
}
