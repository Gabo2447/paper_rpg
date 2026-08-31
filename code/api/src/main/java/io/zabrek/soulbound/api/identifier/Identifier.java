package io.zabrek.soulbound.api.identifier;

/**
 * Represents an identifier used to uniquely identify components or triggers in SoulBound.
 *
 * @since 2.0.0
 */
public interface Identifier {

    /**
     * The string used to separate the package name from the identifier.
     *
     * @since 2.0.0
     */
    String SEPARATOR = ">";

    /**
     * Returns the type of the identifier (e.g., the trigger type category).
     *
     * @return the identifier type
     * @since 2.0.0
     */
    String getType();

    /**
     * Returns the unique key of the identifier within its type.
     *
     * @return the identifier key
     * @since 2.0.0
     */
    String getKey();

    /**
     * Returns the full formatted identifier string combining the type and key.
     *
     * @return the full identifier in the format {@code type>key}
     * @since 2.0.0
     */
    String getFull();
}
