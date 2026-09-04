package io.zabrek.soulbound.database.data;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.config.ConfigAccessor;
import io.zabrek.soulbound.api.data.CooldownRecord;
import io.zabrek.soulbound.api.data.LevelRecord;
import io.zabrek.soulbound.api.identifier.SkillIdentifier;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.profile.Profile;
import io.zabrek.soulbound.api.service.identifier.Identifiers;
import io.zabrek.soulbound.database.Arguments;
import io.zabrek.soulbound.database.Connector;
import io.zabrek.soulbound.database.QueryType;
import io.zabrek.soulbound.database.Saver;
import io.zabrek.soulbound.database.UpdateType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents an object storing all profile-related data, which can load and save it.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class PlayerData {

    /**
     * The profileID of the data.
     */
    private final String profileID;

    /**
     * Custom {@link SoulBoundLogger} instance for this class.
     */
    private final SoulBoundLogger log;

    /**
     * The database saver for player data.
     */
    private final Saver saver;

    /**
     * The database connector.
     */
    private final Connector connector;

    /**
     * Identifier registry to resolve identifiers.
     */
    private final Identifiers identifierRegistry;

    /**
     * The profile this data belongs to.
     */
    private final Profile profile;

    /**
     * The config accessor.
     */
    private final ConfigAccessor config;

    /**
     * List of levels the player has.
     */
    private List<LevelRecord> levels = new CopyOnWriteArrayList<>();

    /**
     * List of cooldowns the player has.
     */
    private List<CooldownRecord> cooldown = new CopyOnWriteArrayList<>();

    /**
     * The language for the profile.
     */
    @Nullable
    private String profileLanguage;

    /**
     * Loads the PlayerData of the given {@link Profile}.
     *
     * @param log                the custom logger for this class
     * @param saver              the saver to persist data changes
     * @param connector          the database connector to use
     * @param profile            the profile to load the data for
     * @param identifierRegistry the identifier registry to resolve identifiers
     * @param config             the config accessor
     */
    public PlayerData(final SoulBoundLogger log, final Saver saver, final Connector connector, final Identifiers identifierRegistry,
                      final Profile profile, final ConfigAccessor config) {
        this.log = log;
        this.saver = saver;
        this.connector = connector;
        this.identifierRegistry = identifierRegistry;
        this.profile = profile;
        this.profileID = profile.getProfileUUID().toString();
        this.config = config;
        this.profileLanguage = null;

        try {
            loadAllPlayerData();
        } catch (final IllegalStateException e) {
            log.error("Could not load player data for %s: %s".formatted(profileID, e.getMessage()));
        }
    }

    private void loadAllPlayerData() {
        log.debug("Loading player data from database for %s".formatted(profileID));
        final Arguments args = new Arguments(profileID);

        connector.querySQL(QueryType.SELECT_COOLDOWN, args, resultSet -> {
            while (resultSet.next()) {
                loadCooldown(resultSet.getString("skill"), resultSet.getString("time"));
            }
            log.debug("Loaded %d cooldowns for %s".formatted(cooldown.size(), profileID));
        }, "Could not load cooldowns.");
        connector.querySQL(QueryType.SELECT_LEVEL, args, resultSet -> {
            while (resultSet.next()) {
                loadLevel(resultSet.getString("skill"), resultSet.getInt("level"), resultSet.getInt("experience"));
            }
            log.debug("Loaded %d level for %s".formatted(levels.size(), profileID));
        }, "Could not load level.");
        connector.querySQL(QueryType.SELECT_PLAYER, args, resultSet -> {
            if (resultSet.next()) {
                profileLanguage = resultSet.getString("language");
                log.debug("Loaded player language '%s' for %s".formatted(profileLanguage, profile));
            } else {
                setupProfile();
            }
        }, "Could not load player data.");
    }

    private void loadCooldown(final String skill, final String time) {
        try {
            final SkillIdentifier identifier = identifierRegistry.getFactory(SkillIdentifier.class).parseIdentifier(skill);
            final CooldownRecord record = new CooldownRecord(identifier, time);
            cooldown.add(record);
        } catch (final SoulBoundException e) {
            log.warn("Loaded '%s' cooldown entry from database, but it is not defined. Skipping...".formatted(skill), e);
        }
    }

    private void loadLevel(final String skill, final double level, final double experience) {
        try {
            final SkillIdentifier identifier = identifierRegistry.getFactory(SkillIdentifier.class).parseIdentifier(skill);
            final LevelRecord record = new LevelRecord(identifier, level, experience);
            levels.add(record);
        } catch (final SoulBoundException e) {
            log.warn("Loaded '%s' levels entry from database, but it is not defined. Skipping...".formatted(skill), e);
        }
    }

    private void setupProfile() {
        log.debug("Profile not found in database. Setting up new profile in database for %s".formatted(profileID));

        final String playerUniqueID = profile.getPlayer().getUniqueId().toString();
        saver.add(new Saver.Record(UpdateType.ADD_PROFILE, profileID));
        saver.add(new Saver.Record(UpdateType.ADD_PLAYER, playerUniqueID, profileID, "default"));
        saver.add(new Saver.Record(UpdateType.ADD_PLAYER_PROFILE, playerUniqueID, profileID,
                config.getString("profile.initial_name", "default")));
    }

    /**
     * Get the cooldowns entries.
     *
     * @return an unmodifiable list of the profiles cooldowns entries
     */
    public List<CooldownRecord> getCooldowns() {
        return (List<CooldownRecord>) copyList(cooldown, new ArrayList<>());
    }

    /**
     * Sets player's cooldown.
     *
     * @param records the cooldowns
     */
    public void setCooldowns(final List<CooldownRecord> records) {
        log.debug("Setting cooldown for %s with %d cooldowns".formatted(profileID, records.size()));
        this.cooldown = (List<CooldownRecord>) copyList(records, new CopyOnWriteArrayList<>());
        refreshCooldown(cooldown);
    }

    /**
     * Adds a new cooldown for the profile.
     *
     * @param record the new cooldown
     */
    public void addCooldown(final CooldownRecord record) {
        log.debug("Adding cooldown for %s".formatted(profileID));
        cooldown.add(record);
        refreshCooldown(cooldown);
    }

    /**
     * Removes a cooldown for the profile.
     *
     * @param record the cooldown to remove
     */
    public void removeCooldown(final CooldownRecord record) {
        log.debug("Removing cooldown for %s".formatted(profileID));
        cooldown.remove(record);
        refreshCooldown(cooldown);
    }

    /**
     * Get the levels entries.
     *
     * @return an unmodifiable list of the profiles levels entries
     */
    public List<LevelRecord> getLevels() {
        return (List<LevelRecord>) copyList(levels, new CopyOnWriteArrayList<>());
    }

    /**
     * Set's the player levels.
     *
     * @param records the levels
     */
    public void setLevels(final List<LevelRecord> records) {
        log.debug("Setting cooldown for %s with %d cooldowns".formatted(profileID, records.size()));
        this.levels = (List<LevelRecord>) copyList(records, new CopyOnWriteArrayList<>());
        refreshLevels(levels);
    }

    /**
     * Add a new level to the player.
     *
     * @param record the level to add
     */
    public void addLevel(final LevelRecord record) {
        log.debug("Adding level for %s".formatted(profileID));
        levels.add(record);
        refreshLevels(levels);
    }

    /**
     * Removes a level to the player.
     *
     * @param record the level
     */
    public void removeLevel(final LevelRecord record) {
        log.debug("Removing level for %s".formatted(profileID));
        levels.remove(record);
        refreshLevels(levels);
    }

    /**
     * Gets player's language.
     *
     * @return the language this profile uses
     */
    public Optional<String> getLanguage() {
        return Optional.ofNullable(profileLanguage);
    }

    /**
     * Sets player's language.
     *
     * @param lang language to set
     */
    public void setLanguage(@Nullable final String lang) {
        if (Objects.equals(profileLanguage, lang)) {
            return;
        }

        log.debug("Setting language for %s to '%s'".formatted(profile, lang));
        this.profileLanguage = lang;
        saver.add(new Saver.Record(UpdateType.UPDATE_PLAYER_LANGUAGE, lang, profileID));
    }

    /**
     * Purges all profile's data from the database and from this object.
     */
    public void purgePlayer() {
        log.debug("Purging all data for %s".formatted(profileID));

        cooldown.clear();
        levels.clear();

        saver.add(new Saver.Record(UpdateType.DELETE_COOLDOWN, profileID));
        saver.add(new Saver.Record(UpdateType.DELETE_LEVEL, profileID));
    }

    private void refreshCooldown(final List<CooldownRecord> records) {
        log.debug("Refreshing cooldown in database for %s (currently %d items)".formatted(profileID, records.size()));
        saver.add(new Saver.Record(UpdateType.DELETE_COOLDOWN, profileID));
        for (final CooldownRecord record : records) {
            saver.add(new Saver.Record(UpdateType.ADD_COOLDOWN, profileID, record.skill(), record.time()));
        }
    }

    private void refreshLevels(final List<LevelRecord> records) {
        log.debug("Refreshing levels in database for %s".formatted(profileID));
        saver.add(new Saver.Record(UpdateType.DELETE_LEVEL, profileID));
        for (final LevelRecord record : records) {
            saver.add(new Saver.Record(UpdateType.ADD_LEVEL, profileID, record.skill(), record.level(), record.experience()));
        }
    }

    private <T> Collection<T> copyList(final Collection<T> source, final Collection<T> target) {
        target.addAll(source);
        return target;
    }
}
