package io.zabrek.soulbound.database;

import java.util.function.Function;

/**
 * Type of the update.
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.ExcessivePublicCount"})
public enum UpdateType {
    /**
     * Add the single skill. ProfileID, skillID, instruction.
     */
    ADD_SKILLS(prefix -> "INSERT INTO " + prefix + "skills (profileID, skill, instructions) VALUES (?, ?, ?);"),

    /**
     * Remove the single skill. ProfileID, triggerID.
     */
    REMOVE_SKILLS(prefix -> "DELETE FROM " + prefix + "skills WHERE profileID = ? AND skill = ?;"),
    ;

    /**
     * Function to create the SQL code from a prefix.
     */
    private final Function<String, String> statementCreator;

    UpdateType(final Function<String, String> statementCreator) {
        this.statementCreator = statementCreator;
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
