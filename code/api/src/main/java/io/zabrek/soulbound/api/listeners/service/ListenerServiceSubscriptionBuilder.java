package io.zabrek.soulbound.api.listeners.service;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.common.function.SoulBoundFunction;
import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.handler.NonProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.OnlineProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.ProfileListenerHandler;
import io.zabrek.soulbound.api.profile.Profile;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.Contract;

import java.util.UUID;

/**
 * The {@link ListenerServiceSubscriptionBuilder} allows creating a subscription with a builder pattern
 * as well as registering it with the {@link ListenerServiceProvider}.
 *
 * @param <E> the event type
 * @since 2.0.0
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface ListenerServiceSubscriptionBuilder<E extends Event> {

    /**
     * Optional build call. Defaults to {@link EventPriority#NORMAL}.
     * <br>
     * Sets the priority to be used for the bukkit event.
     *
     * @param priority the priority to use
     * @return this
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> priority(EventPriority priority);

    /**
     * Required build call. Sets the listener related to the registered event.
     *
     * @param listenerID the listener id
     * @return this
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> source(ListenerIdentifier listenerID);

    /**
     * Required build call. Sets the non-profile handler to be called by the bukkit event.
     * A {@link NonProfileListenerHandler} does not provide any profile information
     * and therefore offering profile-specific functionality.
     *
     * @param handler the handler to use
     * @return this
     * @throws IllegalStateException if another handler is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> handler(NonProfileListenerHandler<E> handler);

    /**
     * Required build call. Sets the profile handler to be called by the bukkit event.
     * A {@link ProfileListenerHandler} provides the profile information for the event retrieved from the extractor.
     *
     * @param handler the handler to use
     * @return this
     * @throws IllegalStateException if another handler is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> handler(ProfileListenerHandler<E> handler);

    /**
     * Required build call. Sets the online profile handler to be called by the bukkit event.
     * A {@link OnlineProfileListenerHandler} provides the profile information for the event retrieved from the extractor.
     *
     * @param handler the handler to use
     * @return this
     * @throws IllegalStateException if another handler is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> onlineHandler(OnlineProfileListenerHandler<E> handler);

    /**
     * Sets the extractor to be used for extracting the player uuid from the event.
     * This call is not required if the {@link #handler(NonProfileListenerHandler)} call is used.
     *
     * @param extractor the extractor to use
     * @return this
     * @throws IllegalStateException if another extractor is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> uuid(SoulBoundFunction<E, UUID> extractor);

    /**
     * Sets the extractor to be used for extracting the player from the event.
     * This call is not required if the {@link #handler(NonProfileListenerHandler)} call is used.
     *
     * @param extractor the extractor to use
     * @return this
     * @throws IllegalStateException if another extractor is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> offlinePlayer(SoulBoundFunction<E, OfflinePlayer> extractor);

    /**
     * Sets the extractor to be used for extracting the player from the event.
     * This call is not required if the {@link #handler(NonProfileListenerHandler)} call is used.
     *
     * @param extractor the extractor to use
     * @return this
     * @throws IllegalStateException if another extractor is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> player(SoulBoundFunction<E, Player> extractor);

    /**
     * Sets the extractor to be used for extracting the player from the event.
     * This call is not required if the {@link #handler(NonProfileListenerHandler)} call is used.
     *
     * @param extractor the extractor to use
     * @return this
     * @throws IllegalStateException if another extractor is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> entity(SoulBoundFunction<E, Entity> extractor);

    /**
     * Sets the extractor to be used for extracting the player from the event.
     * This call is not required if the {@link #handler(NonProfileListenerHandler)} call is used.
     *
     * @param extractor the extractor to use
     * @return this
     * @throws IllegalStateException if another extractor is already set
     * @since 2.0.0
     */
    @Contract("_ -> this")
    ListenerServiceSubscriptionBuilder<E> profile(SoulBoundFunction<E, Profile> extractor);

    /**
     * Required last build call. Registers the subscription with the {@link ListenerServiceProvider}.
     *
     * @param ignoreCancelled if canceled events should be ignored
     * @throws SoulBoundException    if the subscription could not be registered
     * @throws IllegalStateException if no valid handler-extractor pair was set
     * @since 2.0.0
     */
    void subscribe(boolean ignoreCancelled) throws SoulBoundException;
}
