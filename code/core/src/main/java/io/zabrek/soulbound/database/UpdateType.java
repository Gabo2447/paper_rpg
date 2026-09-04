package io.zabrek.soulbound.database;

import java.util.function.Function;

/**
 * Type of the update.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public enum UpdateType {
    /**
     * Add the single trigger. ProfileID, triggerID, instruction.
     */
    ADD_TRIGGERS(prefix -> "INSERT INTO " + prefix + "triggers (profileID, trigger, instructions) VALUES (?, ?, ?);"),
    /**
     * Add single player profile. PlayerID, profileID, name.
     */
    ADD_PLAYER_PROFILE(prefix -> "INSERT INTO " + prefix + "player_profile (playerID, profileID, name) VALUES (?, ?, ?);"),
    /**
     * Add single profile. ProfileID.
     */
    ADD_PROFILE(prefix -> "INSERT INTO " + prefix + "profile (profileID) VALUES (?);"),
    /**
     * Add single cooldown. ProfileID, skill, time.
     */
    ADD_COOLDOWN(prefix -> "INSERT INTO " + prefix + "cooldown (profileID, skill, time) VALUES (?, ?, ?);"),
    /**
     * Add a single level. ProfileID, skill, level, experience.
     */
    ADD_LEVEL(prefix -> "INSERT INTO " + prefix + "level (profileID, skill, level, experience) VALUES(?, ?, ?, ?);"),
    /**
     * Add single player. PlayerID, active_profile, language.
     */
    ADD_PLAYER(prefix -> "INSERT INTO " + prefix + "player (playerID, active_profile, language) VALUES (?, ?);"),

    /**
     * Removes the single trigger. ProfileID, triggerID.
     */
    REMOVE_TRIGGERS(prefix -> "DELETE FROM " + prefix + "triggers WHERE profileID = ? AND trigger = ?;"),
    /**
     * Remove single player profile. ProfileID.
     */
    REMOVE_PLAYER_PROFILE(prefix -> "DELETE FROM " + prefix + "player_profile WHERE profileID = ?;"),
    /**
     * Remove single profile. ProfileID.
     */
    REMOVE_PROFILE(prefix -> "DELETE FROM " + prefix + "profile WHERE profileID = ?;"),
    /**
     * Remove single cooldown. ProfileID, skill.
     */
    REMOVE_COOLDOWN(prefix -> "DELETE FROM " + prefix + "cooldown WHERE profileID = ? AND skill = ?;"),
    /**
     * Remove single level. ProfileID, skill.
     */
    REMOVE_LEVEL(prefix -> "DELETE FROM " + prefix + "level WHERE profileID = ? AND skill = ?;"),

    /**
     * Deletes all triggers for a given profile. ProfileID.
     */
    DELETE_TRIGGERS(prefix -> "DELETE FROM " + prefix + "triggers WHERE profileID = ?;"),
    /**
     * Deletes all cooldown for a give profile. ProfileID.
     */
    DELETE_COOLDOWN(prefix -> "DELETE FROM " + prefix + "cooldown WHERE profileID = ?;"),
    /**
     * Deletes all level for a give profile. ProfileID.
     */
    DELETE_LEVEL(prefix -> "DELETE FROM " + prefix + "level WHERE profileID = ?;"),
    /**
     * Deletes the player. PlayerID.
     */
    DELETE_PLAYER(prefix -> "DELETE FROM " + prefix + "player WHERE playerID = ?;"),

    /**
     * Updates the profileID of all triggers for a given profile. ProfileID, ProfileID.
     */
    UPDATE_PLAYERS_TRIGGERS(prefix -> "UPDATE " + prefix + "triggers SET profileID = ? WHERE profileID = ?;"),
    /**
     * Updates the profileID's name for a given profile. Name, ProfileID.
     */
    UPDATE_PROFILE_NAME(prefix -> "UPDATE " + prefix + "player_profile SET name = ? WHERE profileID = ?;"),
    /**
     * Updates the player's level for a given profile. Level, ProfileID, Skill.
     */
    UPDATE_PLAYERS_LEVEL(prefix -> "UPDATE " + prefix + "level SET level = ? WHERE profileID = ? AND skill = ?;"),
    /**
     * Updates the player's cooldown for a given profile and skill. Time, ProfileID, Skill.
     */
    UPDATE_PLAYERS_COOLDOWN(prefix -> "UPDATE " + prefix + "cooldown SET time = ? WHERE profileID = ? AND skill = ?;"),
    /**
     * Updates the profileID's language for a given profile. Language, ProfileID.
     */
    UPDATE_PLAYER_LANGUAGE(prefix -> "UPDATE " + prefix + "player SET language = ? WHERE playerID = ?;"),

    /**
     * Drops the triggers table.
     */
    DROP_TRIGGERS(prefix -> "DROP TABLE " + prefix + "triggers"),
    /**
     * Drops the player profile table.
     */
    DROP_PLAYER_PROFILE(prefix -> "DROP TABLE " + prefix + "player_profile;"),
    /**
     * Drops the profile table.
     */
    DROP_PROFILE(prefix -> "DROP TABLE " + prefix + "profile;"),
    /**
     * Drops the player table.
     */
    DROP_PLAYER(prefix -> "DROP TABLE " + prefix + "player"),
    /**
     * Drops the cooldown table.
     */
    DROP_COOLDOWN(prefix -> "DROP TABLE " + prefix + "cooldown;"),
    /**
     * Drops the level table.
     */
    DROP_LEVEL(prefix -> "DROP TABLE " + prefix + "level;"),
    /**
     * Drops the migration table.
     */
    DROP_MIGRATION(prefix -> "DROP TABLE " + prefix + "migration"),

    /**
     * Inserts a new trigger. ProfileID, trigger, instructions.
     */
    INSERT_TRIGGER(prefix -> "INSERT INTO " + prefix + "triggers (profileID, trigger, instructions) VALUES (?, ?, ?);"),
    /**
     * Inserts a new player. PlayerID, active_profile, language.
     */
    INSERT_PLAYER(prefix -> "INSERT INTO " + prefix + "player (playerID, active_profile, language) VALUES (?,?,?);"),
    /**
     * Inserts a new profile. ProfileID.
     */
    INSERT_PROFILE(prefix -> "INSERT INTO " + prefix + "profile (profileID) VALUES (?);"),
    /**
     * Inserts a new player profile. PlayerID, profileID, name.
     */
    INSERT_PLAYER_PROFILE(prefix -> "INSERT INTO " + prefix + "player_profile (playerID, profileID, name) VALUES (?,?,?);"),
    /**
     * Inserts a new cooldown. ProfileID, skill, time.
     */
    INSERT_COOLDOWN(prefix -> "INSERT INTO " + prefix + "cooldown (profileID, skill, time) VALUES (?, ?, ?);"),
    /**
     * Inserts a new level. ProfileID, skill, level, experience.
     */
    INSERT_LEVEL(prefix -> "INSERT INTO " + prefix + "level (profileID, skill, level, experience) VALUES(?, ?, ?, ?);"),

    /**
     * Remove all triggers for a given trigger. Trigger.
     */
    REMOVE_ALL_TRIGGERS(prefix -> "DELETE FROM " + prefix + "triggers WHERE trigger = ?;"),

    /**
     * Renames all triggers for a given trigger. Trigger, Trigger.
     */
    RENAME_ALL_TRIGGERS(prefix -> "UPDATE " + prefix + "triggers SET trigger = ? WHERE trigger = ?");

    /**
     * Function to create the SQL code from a prefix.
     */
    private final Function<String, String> statementCreator;

    UpdateType(final Function<String, String> sqlTemplate) {
        this.statementCreator = sqlTemplate;
    }

    /**
     * Create the SQL code for the given table prefix.
     *
     * @param tablePrefix table prefix to use
     * @return SQL-code for the update
     */
    public String createSql(final String tablePrefix) {
        return statementCreator.apply(tablePrefix);
    }
}
