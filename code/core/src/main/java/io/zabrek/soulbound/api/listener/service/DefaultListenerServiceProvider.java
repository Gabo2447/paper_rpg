package io.zabrek.soulbound.api.listener.service;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.bukkit.event.BukkitEventService;
import io.zabrek.soulbound.api.bukkit.event.EventServiceSubscriber;
import io.zabrek.soulbound.api.common.function.SoulBoundBiFunction;
import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.handler.NonProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.OnlineProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.handler.ProfileListenerHandler;
import io.zabrek.soulbound.api.listeners.service.ListenerService;
import io.zabrek.soulbound.api.listeners.service.ListenerServiceProvider;
import io.zabrek.soulbound.api.listeners.service.ListenerServiceSubscriptionBuilder;
import io.zabrek.soulbound.api.logger.SoulBoundExceptionHandler;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.api.profile.OnlineProfile;
import io.zabrek.soulbound.api.profile.Profile;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import io.zabrek.soulbound.data.PlayerDataStorage;
import io.zabrek.soulbound.lib.bukkit.event.DefaultBukkitEventService;
import io.zabrek.soulbound.lib.logger.DefaultSoulBoundExceptionHandler;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The default implementation of the {@link ListenerServiceProvider}.
 */
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class DefaultListenerServiceProvider implements ListenerServiceProvider {

    /**
     * The event service to register events with.
     */
    private final BukkitEventService eventService;

    /**
     * The profile provider to get the profile of a player.
     */
    private final ProfileProvider profileProvider;

    /**
     * The logger for this service.
     */
    private final SoulBoundLogger logger;

    /**
     * The logger factory to inject into other services.
     */
    private final SoulBoundLoggerFactory loggerFactory;

    /**
     * The map holding the objectives service data.
     */
    private final Map<ListenerIdentifier, DefaultListenerService> services;

    /**
     * The player data storage.
     */
    private final PlayerDataStorage playerDataStorage;

    /**
     * Sole constructor. Creates an objective event service on top of a {@link BukkitEventService}.
     *
     * @param loggerFactory     the logger factory
     * @param profileProvider   the profile provider
     * @param plugin            the plugin instance
     * @param playerDataStorage the player data storage
     */
    public DefaultListenerServiceProvider(final SoulBoundLoggerFactory loggerFactory, final ProfileProvider profileProvider,
                                          final Plugin plugin, final PlayerDataStorage playerDataStorage) {
        this.eventService = new DefaultBukkitEventService(plugin, loggerFactory);
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.create(DefaultListenerServiceProvider.class);
        this.profileProvider = profileProvider;
        this.playerDataStorage = playerDataStorage;
        this.services = new HashMap<>();
    }

    @Override
    public void clear() {
        eventService.unsubscribeAll();
        services.clear();
    }

    @Override
    public ListenerService getFactoryService(final ListenerIdentifier listenerID) throws SoulBoundException {
        if (services.containsKey(listenerID)) {
            return services.get(listenerID);
        }
        final DefaultListenerService service = new DefaultListenerService(listenerID, this, loggerFactory, profileProvider, playerDataStorage);
        services.put(listenerID, service);
        return service;
    }

    @Override
    public <E extends Event> ListenerServiceSubscriptionBuilder<E> request(final Class<E> eventClass) {
        return new DefaultListenerServiceSubscriptionBuilder<>(this, eventClass);
    }

    @Override
    public <E extends Event> void subscribe(final ListenerIdentifier listenerID, final Class<E> eventClass, final NonProfileListenerHandler<E> handler,
                                            final EventPriority priority, final boolean ignoreCanceled) throws SoulBoundException {
        if (!eventService.require(eventClass, priority)) {
            throw new SoulBoundException("<%s> Could not subscribe to event '%s'".formatted(listenerID, eventClass.getSimpleName()));
        }
        final EventServiceSubscriber<E> subscriber = subNonProfile(handler);
        final EventServiceSubscriber<E> exceptionHandled = exceptionHandled(listenerID, eventClass, subscriber);
        eventService.subscribe(eventClass, priority, ignoreCanceled, exceptionHandled);
        logger.debug(listenerID::getFull, "Subscribe to event '%s' with priority '%s' and ignoreCanceled '%b'".formatted(eventClass.getSimpleName(), priority.name(), ignoreCanceled));
    }

    @Override
    public <E extends Event> void subscribe(final ListenerIdentifier listenerID, final Class<E> eventClass, final ProfileListenerHandler<E> handler,
                                            final SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor,
                                            final EventPriority priority, final boolean ignoreCanceled) throws SoulBoundException {
        if (!eventService.require(eventClass, priority)) {
            throw new SoulBoundException("<%s> Could not subscribe to event '%s'".formatted(listenerID, eventClass.getSimpleName()));
        }
        final EventServiceSubscriber<E> subscriber = subOffline(handler, profileExtractor);
        final EventServiceSubscriber<E> exceptionHandled = exceptionHandled(listenerID, eventClass, subscriber);
        eventService.subscribe(eventClass, priority, ignoreCanceled, exceptionHandled);
        logger.debug(listenerID::getFull, "Subscribe to event '%s' with priority '%s' and ignoreCanceled '%b'".formatted(eventClass.getSimpleName(), priority.name(), ignoreCanceled));
    }

    @Override
    public <E extends Event> void subscribe(final ListenerIdentifier listenerID, final Class<E> eventClass, final OnlineProfileListenerHandler<E> handler,
                                            final SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor,
                                            final EventPriority priority, final boolean ignoreCanceled) throws SoulBoundException {
        if (!eventService.require(eventClass, priority)) {
            throw new SoulBoundException("<%s> Could not subscribe to event '%s'".formatted(listenerID, eventClass.getSimpleName()));
        }
        final EventServiceSubscriber<E> subscriber = subOnline(handler, profileExtractor);
        final EventServiceSubscriber<E> exceptionHandled = exceptionHandled(listenerID, eventClass, subscriber);
        eventService.subscribe(eventClass, priority, ignoreCanceled, exceptionHandled);
        logger.debug(listenerID::getFull, "Subscribe to event '%s' with priority '%s' and ignoreCanceled '%b'".formatted(eventClass.getSimpleName(), priority.name(), ignoreCanceled));
    }

    private <E extends Event> EventServiceSubscriber<E> exceptionHandled(final ListenerIdentifier listenerID, final Class<E> eventClass,
                                                                         final EventServiceSubscriber<E> subscriber) {
        final SoulBoundExceptionHandler exceptionHandler = new DefaultSoulBoundExceptionHandler(listenerID::getFull, logger, eventClass.getSimpleName());
        return (event, priority) -> exceptionHandler.handle(() -> subscriber.call(event, priority));
    }

    private <E extends Event> EventServiceSubscriber<E> subNonProfile(final NonProfileListenerHandler<E> eventHandler) {
        return (event, priority) -> eventHandler.handle(event);
    }

    private <E extends Event> EventServiceSubscriber<E> subOnline(final OnlineProfileListenerHandler<E> handler,
                                                                  final SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor) {
        return (event, priority) -> {
            final Optional<Profile> profile = profileExtractor.apply(profileProvider, event);
            if (profile.isEmpty()) {
                return;
            }
            final Optional<OnlineProfile> onlineProfile = profile.get().getOnlineProfile();
            if (onlineProfile.isEmpty()) {
                return;
            }
            final OnlineProfile executingProfile = onlineProfile.get();
            handler.handle(event, executingProfile);
        };
    }

    private <E extends Event> EventServiceSubscriber<E> subOffline(final ProfileListenerHandler<E> handler,
                                                                   final SoulBoundBiFunction<ProfileProvider, E, Optional<Profile>> profileExtractor) {
        return (event, priority) -> {
            final Optional<Profile> profile = profileExtractor.apply(profileProvider, event);
            if (profile.isEmpty()) {
                return;
            }
            final Profile executingProfile = profile.get();
            handler.handle(event, executingProfile);
        };
    }
}
