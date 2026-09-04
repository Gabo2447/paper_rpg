package io.zabrek.soulbound.api.listener.service;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.listeners.service.ListenerDataService;
import io.zabrek.soulbound.api.listeners.service.ListenerService;
import io.zabrek.soulbound.api.listeners.service.ListenerServiceProvider;
import io.zabrek.soulbound.api.listeners.service.ListenerServiceSubscriptionBuilder;
import io.zabrek.soulbound.api.logger.SoulBoundExceptionHandler;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import io.zabrek.soulbound.data.PlayerDataStorage;
import io.zabrek.soulbound.lib.logger.DefaultSoulBoundExceptionHandler;
import org.bukkit.event.Event;

/**
 * Default implementation of the {@link ListenerService}.
 */
public class DefaultListenerService implements ListenerService {

    /**
     * The listener service data.
     */
    private final ListenerDataService listenerDataService;

    /**
     * The listener service.
     */
    private final ListenerServiceProvider listenerService;

    /**
     * The exception handler for this service.
     */
    private final SoulBoundExceptionHandler soulExceptionHandler;

    /**
     * The logger for this service.
     */
    private final SoulBoundLogger logger;

    /**
     * The profile provider.
     */
    private final ProfileProvider profileProvider;

    /**
     * The listener related to this service.
     */
    private final ListenerIdentifier listenerID;

    /**
     * Creates a new listener service.
     *
     * @param listenerID        the listener related to this service
     * @param listenerService   the event service to request events from
     * @param loggerFactory     the logger factory to use
     * @param profileProvider   the profile provider to use
     * @param playerDataStorage the player data storage
     * @throws SoulBoundException if the listener service data of the instruction could not be parsed
     */
    public DefaultListenerService(final ListenerIdentifier listenerID, final ListenerServiceProvider listenerService,
                                  final SoulBoundLoggerFactory loggerFactory, final ProfileProvider profileProvider,
                                  final PlayerDataStorage playerDataStorage) throws SoulBoundException {
        this.listenerID = listenerID;
        this.listenerService = listenerService;
        this.logger = loggerFactory.create(SoulBoundExceptionHandler.class);
        this.profileProvider = profileProvider;
        this.listenerDataService = new DefaultListenerDataService(playerDataStorage);
        this.soulExceptionHandler = new DefaultSoulBoundExceptionHandler(listenerID::getFull, this.logger);
    }

    @Override
    public <T extends Event> ListenerServiceSubscriptionBuilder<T> request(final Class<T> eventClass) {
        return listenerService.request(eventClass).source(listenerID);
    }

    @Override
    public SoulBoundExceptionHandler getExceptionHandler() {
        return soulExceptionHandler;
    }

    @Override
    public SoulBoundLogger getLogger() {
        return logger;
    }

    @Override
    public ProfileProvider getProfileProvider() {
        return profileProvider;
    }

    @Override
    public ListenerDataService getData() {
        return listenerDataService;
    }

    @Override
    public ListenerIdentifier getListenerID() {
        return listenerID;
    }
}
