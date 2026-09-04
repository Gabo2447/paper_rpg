package io.zabrek.soulbound.api.listener.service;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.common.function.SoulBoundBiFunction;
import io.zabrek.soulbound.api.common.function.SoulBoundFunction;
import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.handler.NonProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.OnlineProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.ProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.service.ListenerServiceSubscriptionBuilder;
import io.zabrek.soulbound.api.profile.Profile;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * The default implementation of the {@link ListenerServiceSubscriptionBuilder}.
 * Requires a {@link NonProfileListenerHandler} or {@link ProfileListenerHandler} to be set before subscribing.
 * <br>
 * Default priority is {@link EventPriority#NORMAL}.
 * <br>
 * Default ignoreCancelled is <code>true</code>.
 *
 * @param <E> the event type
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals"})
public class DefaultListenerServiceSubscriptionBuilder<E extends Event> implements ListenerServiceSubscriptionBuilder<E> {

    /**
     * The default priority for event subscriptions.
     */
    public static final EventPriority DEFAULT_PRIORITY = EventPriority.NORMAL;

    /**
     * The event service to register events with.
     */
    private final DefaultListenerServiceProvider eventService;

    /**
     * The event class to register.
     */
    private final Class<E> eventClass;

    /**
     * The priority of the event.
     */
    private EventPriority eventPriority;

    /**
     * The listener related to this event.
     */
    @Nullable
    private ListenerIdentifier listenerID;

    /**
     * Whether to ignore canceled events.
     */
    private boolean ignoreCanceled;

    /**
     * The non-profile event handler.
     */
    @Nullable
    private NonProfileListenerHandler<E> nonProfileHandler;

    /**
     * The profile event handler. Requires an extractor to be set.
     */
    @Nullable
    private ProfileListenerHandler<E> profileHandler;

    /**
     * The online profile event handler. Requires an extractor to be set.
     */
    @Nullable
    private OnlineProfileListenerHandler<E> onlineProfileHandler;

    /**
     * The player UUID extractor. Required for {@link ProfileListenerHandler}s.
     */
    @Nullable
    private SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor;

    /**
     * Creates a new builder for the given event class.
     *
     * @param eventService the event service to register events with
     * @param eventClass   the event class to register
     */
    public DefaultListenerServiceSubscriptionBuilder(final DefaultListenerServiceProvider eventService, final Class<E> eventClass) {
        this.eventService = eventService;
        this.eventClass = eventClass;
        this.listenerID = null;
        this.eventPriority = DEFAULT_PRIORITY;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> priority(final EventPriority priority) {
        this.eventPriority = priority;
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> source(final ListenerIdentifier listenerID) {
        this.listenerID = listenerID;
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> handler(final NonProfileListenerHandler<E> handler) {
        if (checkHandlerAlreadySet()) {
            throw new IllegalStateException("Handler already set");
        }
        this.nonProfileHandler = handler;
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> handler(final ProfileListenerHandler<E> handler) {
        if (checkHandlerAlreadySet()) {
            throw new IllegalStateException("Handler already set");
        }
        this.profileHandler = handler;
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> onlineHandler(final OnlineProfileListenerHandler<E> handler) {
        if (checkHandlerAlreadySet()) {
            throw new IllegalStateException("Handler already set");
        }
        this.onlineProfileHandler = handler;
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> uuid(final SoulBoundFunction<E, UUID> extractor) {
        if (this.profileExtractor != null) {
            throw new IllegalStateException("Cannot set more than one extractor!");
        }
        this.profileExtractor = (provider, event) -> Optional.ofNullable(extractor.apply(event)).map(provider::getProfile);
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> offlinePlayer(final SoulBoundFunction<E, OfflinePlayer> extractor) {
        if (this.profileExtractor != null) {
            throw new IllegalStateException("Cannot set more than one extractor!");
        }
        this.profileExtractor = (provider, event) -> Optional.ofNullable(extractor.apply(event)).map(provider::getProfile);
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> player(final SoulBoundFunction<E, Player> extractor) {
        if (this.profileExtractor != null) {
            throw new IllegalStateException("Cannot set more than one extractor!");
        }
        this.profileExtractor = (provider, event) -> Optional.ofNullable(extractor.apply(event)).map(provider::getProfile);
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> entity(final SoulBoundFunction<E, Entity> extractor) {
        if (this.profileExtractor != null) {
            throw new IllegalStateException("Cannot set more than one extractor!");
        }
        this.profileExtractor = (provider, event) -> Optional.ofNullable(extractor.apply(event))
                .map(entity -> entity instanceof final Player player ? provider.getProfile(player) : null);
        return this;
    }

    @Override
    public ListenerServiceSubscriptionBuilder<E> profile(final SoulBoundFunction<E, Profile> extractor) {
        if (this.profileExtractor != null) {
            throw new IllegalStateException("Cannot set more than one extractor!");
        }
        this.profileExtractor = (provider, event) -> Optional.ofNullable(extractor.apply(event));
        return this;
    }

    @Override
    public void subscribe(final boolean ignoreCancelled) throws SoulBoundException {
        this.ignoreCanceled = ignoreCancelled;
        subscribe();
    }

    private void subscribe() throws SoulBoundException {
        if (this.listenerID == null) {
            throw new SoulBoundException("Listener ID is not specified!");
        }
        if (this.nonProfileHandler != null) {
            eventService.subscribe(listenerID, eventClass, nonProfileHandler, eventPriority, ignoreCanceled);
            return;
        }
        if (profileExtractor == null) {
            throw new IllegalStateException("No valid extractor specified!");
        }
        if (onlineProfileHandler != null) {
            eventService.subscribe(listenerID, eventClass, onlineProfileHandler, profileExtractor, eventPriority, ignoreCanceled);
            return;
        }
        if (profileHandler != null) {
            eventService.subscribe(listenerID, eventClass, profileHandler, profileExtractor, eventPriority, ignoreCanceled);
            return;
        }
        throw new IllegalStateException("No valid handler specified!");
    }

    private boolean checkHandlerAlreadySet() {
        return nonProfileHandler != null || profileHandler != null || onlineProfileHandler != null;
    }
}
