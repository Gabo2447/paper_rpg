package io.zabrek.soulbound.api.listeners.service;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.common.function.SoulBoundBiFunction;
import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.handler.NonProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.OnlineProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.ProfileListenerHandler;
import io.zabrek.soulbound.api.profile.Profile;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.Optional;

/**
 * The service for listeners managing the subscription of event handlers.
 *
 * @since 2.0.0
 */
public interface ListenerServiceProvider {

    /**
     * Resets the entire service.
     * Use with caution!
     *
     * @since 2.0.0
     */
    void clear();

    /**
     * Creates a new {@link ListenerService} for the given listenerID.
     *
     * @param listenerID the listener to create a subscription service for
     * @return a new {@link ListenerService} for the given listenerID
     * @throws SoulBoundException if the listener causes issues with creating a factory service
     * @since 2.0.0
     */
    ListenerService getFactoryService(ListenerIdentifier listenerID) throws SoulBoundException;

    /**
     * Requests a new event subscription using an {@link ListenerServiceSubscriptionBuilder}.
     * The request may be completed in one chain of calls requiring at least a handler and ending with
     * {@link ListenerServiceSubscriptionBuilder#subscribe(boolean)}.
     *
     * @param eventClass the event class to subscribe to
     * @param <E>        the event type
     * @return a new {@link ListenerServiceSubscriptionBuilder} for the requested event
     * @since 2.0.0
     */
    <E extends Event> ListenerServiceSubscriptionBuilder<E> request(Class<E> eventClass);

    /**
     * Registers a new event subscription for a specific non-profile event.
     *
     * @param listenerID      the listener to subscribe to
     * @param eventClass      the class of the event to subscribe to
     * @param handler         the handler to call when the event is triggered
     * @param priority        the priority of the event listener
     * @param ignoreCancelled if the event should be ignored if canceled
     * @param <E>             the event type
     * @throws SoulBoundException if the event could not be subscribed
     * @since 2.0.0
     */
    <E extends Event> void subscribe(ListenerIdentifier listenerID, Class<E> eventClass, NonProfileListenerHandler<E> handler,
                                     EventPriority priority, boolean ignoreCancelled) throws SoulBoundException;

    /**
     * Registers a new event subscription for a specific event with a profile involved.
     *
     * @param listenerID       the listener to subscribe to
     * @param eventClass       the event class to subscribe to
     * @param handler          the handler to call when the event is triggered
     * @param profileExtractor a method to extract the profile from the event
     * @param priority         the priority of the event listener
     * @param ignoreCancelled  if the event should be ignored if canceled
     * @param <E>              the event type
     * @throws SoulBoundException if the event could not be subscribed
     * @since 2.0.0
     */
    <E extends Event> void subscribe(ListenerIdentifier listenerID, Class<E> eventClass, ProfileListenerHandler<E> handler,
                                     SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor,
                                     EventPriority priority, boolean ignoreCancelled) throws SoulBoundException;

    /**
     * Registers a new event subscription for a specific event with a profile involved.
     *
     * @param listenerID       the listener to subscribe to
     * @param eventClass       the event class to subscribe to
     * @param handler          the handler to call when the event is triggered
     * @param profileExtractor a method to extract the profile from the event
     * @param priority         the priority of the event listener
     * @param ignoreCancelled  if the event should be ignored if canceled
     * @param <E>              the event type
     * @throws SoulBoundException if the event could not be subscribed
     * @since 2.0.0
     */
    <E extends Event> void subscribe(ListenerIdentifier listenerID, Class<E> eventClass, OnlineProfileListenerHandler<E> handler,
                                     SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor,
                                     EventPriority priority, boolean ignoreCancelled) throws SoulBoundException;
}
