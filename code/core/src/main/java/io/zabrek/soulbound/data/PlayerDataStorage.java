package io.zabrek.soulbound.data;

import dev.faststats.data.Metric;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.profile.Profile;
import io.zabrek.soulbound.api.profile.ProfileProvider;
import io.zabrek.soulbound.database.data.PlayerData;
import io.zabrek.soulbound.database.data.PlayerDataFactory;
import io.zabrek.soulbound.faststats.FastStatsMetricsProvider;
import io.zabrek.soulbound.lib.profile.ProfileKeyMap;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores loaded {@link PlayerData}.
 */
public class PlayerDataStorage implements FastStatsMetricsProvider {

    /**
     * Custom logger for debug messages.
     */
    private final SoulBoundLogger log;

    /**
     * Factory to create new Player Data.
     */
    private final PlayerDataFactory playerDataFactory;

    /**
     * Stored player data for online players.
     */
    private final Map<Profile, PlayerData> playerDataMap;

    /**
     * Create a new Storage for Player Data.
     *
     * @param log               the logger for debug messages
     * @param playerDataFactory the factory to create player data
     * @param profileProvider   the profile provider to use
     */
    public PlayerDataStorage(final SoulBoundLogger log, final PlayerDataFactory playerDataFactory,
                             final ProfileProvider profileProvider) {
        this.log = log;
        this.playerDataFactory = playerDataFactory;
        this.playerDataMap = new ProfileKeyMap<>(profileProvider, new ConcurrentHashMap<>());
    }

    /**
     * Creates new PlayerData and stores it.
     *
     * @param profile the {@link Profile} of the player
     * @return the created PlayerData
     */
    public PlayerData init(final Profile profile) {
        log.debug("Initializing PlayerData for profile: %s".formatted(profile));
        return playerDataMap.computeIfAbsent(profile, playerDataFactory::createPlayerData);
    }

    /**
     * Retrieves PlayerData object for the specified profile. If the playerData does
     * not exist, it will create a new playerData.
     * If the player is online, it will be stored as well.
     *
     * @param profile the {@link Profile} of the player
     * @return PlayerData object for the player
     */
    public PlayerData get(final Profile profile) {
        log.debug("Getting PlayerData for %s (cached=%s, online=%s)".formatted(profile, playerDataMap.containsKey(profile), profile.getOnlineProfile().isPresent()));
        final PlayerData playerData = playerDataMap.get(profile);
        if (playerData != null) {
            return playerData;
        }
        if (profile.getOnlineProfile().isPresent()) {
            return init(profile);
        }
        return playerDataFactory.createPlayerData(profile);
    }

    /**
     * Removes the database playerData from the map.
     *
     * @param profile the {@link Profile} of the player whose playerData is to be removed
     */
    public void remove(final Profile profile) {
        log.debug("Removing PlayerData from storage for %s".formatted(profile));
        playerDataMap.remove(profile);
    }

    @Override
    public Set<Metric<?>> getMetrics() {
        return Set.of(
                Metric.number("profiles_personal_lang_count", () -> playerDataMap.values().stream()
                        .filter(data -> data.getLanguage().isPresent()
                                && !"default".equalsIgnoreCase(data.getLanguage().get())).count()),
                Metric.stringArray("profiles_personal_lang", () -> playerDataMap.values().stream()
                        .map(data -> data.getLanguage().orElse(null)).filter(Objects::nonNull)
                        .filter(lang -> !"default".equalsIgnoreCase(lang))
                        .toList().toArray(new String[0]))
        );
    }
}
