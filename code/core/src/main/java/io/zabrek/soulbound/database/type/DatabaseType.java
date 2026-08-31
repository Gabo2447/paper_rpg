package io.zabrek.soulbound.database.type;

import org.jetbrains.annotations.Nullable;

/**
 * Represents the supported types of databases for data storage.
 */
public enum DatabaseType {
    /**
     * SQLite database type.
     */
    SQLITE,

    /**
     * MySQL database type.
     */
    MYSQL;

    /**
     * Converts a string value to the corresponding DatabaseType.
     *
     * @param value the string representation of the database type
     * @return the matching DatabaseType, or SQLITE if the value is empty (default value)
     */
    @Nullable
    public static DatabaseType fromString(final String value) {
        if (value.isBlank()) {
            return null;
        }

        for (final DatabaseType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return null;
    }
}
